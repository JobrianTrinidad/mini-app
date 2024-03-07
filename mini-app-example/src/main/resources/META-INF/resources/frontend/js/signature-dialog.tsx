import { html, LitElement } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import { ifDefined } from 'lit/directives/if-defined.js';
import { ImageRequest, SaveImageResponse, getImageById, saveImage } from 'Frontend/generated/jar-resources/js/imageService.tsx';

import '@vaadin/button';
import '@vaadin/dialog';
import '@vaadin/email-field';
import '@vaadin/icon';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset';
import '@vaadin/text-field';
import '@vaadin/vertical-layout';
import { dialogFooterRenderer, dialogRenderer } from '@vaadin/dialog/lit.js';
import type { DialogOpenedChangedEvent } from '@vaadin/dialog';
import { applyTheme } from 'Frontend/generated/theme';

@customElement('signature-dialog')
export class Example extends LitElement {

  @state()
  private dialogOpened = false;
 @state()
  private showViewButton = false;
  private signaturePad: any = null;
  private imageData: string | null = null;

  protected override createRenderRoot() {
    const root = super.createRenderRoot();
    applyTheme(root);
    return root;
  }

  protected override render() {
    return html`
      <vaadin-dialog
        header-title="Signature"
        .opened="${this.dialogOpened}"
        @opened-changed="${(event: DialogOpenedChangedEvent) => {
          this.dialogOpened = event.detail.value;
        }}"
        ${dialogRenderer(this.renderDialog, [])}
        ${dialogFooterRenderer(this.renderFooter, [])}
      ></vaadin-dialog>

      <vaadin-button theme="primary"
       @click="${this.open}"
      >
     Signature
      </vaadin-button>


          `;
  }

  private renderDialog = () => html`
    <lit-signature-pad
      id="signaturePad"
      @ready="${this.handleSignaturePadReady}"
    >
    <img src="${this.imageSrc}" alt="Signature">
    </lit-signature-pad>




  `;

  private renderFooter = () => html`
    <vaadin-button @click="${this.clearSignature}">Reset</vaadin-button>
    <vaadin-button theme="primary" @click="${this.saveSignature}">Save</vaadin-button>
    <vaadin-button theme="primary" @click="${this.close}">Close</vaadin-button>

  `;
  private loadImage(imageSrc: string) {
  this.dialogOpened = true
    this.imageSrc = imageSrc;
  }
  private open() {
    this.dialogOpened = true;

  }

  private close() {
    this.dialogOpened = false;
  }

  private handleSignaturePadReady(event: Event) {
    this.signaturePad = event.target;
    this.signaturePadInitialized = true;
  }

  private clearSignature() {
    if (this.signaturePadInitialized) {
      if (this.signaturePad) {
        this.signaturePad.clear();
      }
    }
  }

  private saveSignature() {
    if (this.signaturePadInitialized) {
      if (this.signaturePad) {
        let base64String = this.signaturePad.getEncodeImage();
        const prefix = "data:image";
        if (base64String.startsWith(prefix)) {
          base64String = base64String.slice(base64String.indexOf(',') + 1);
        }

        const myImage: ImageRequest = {
          name: "My Image", // TODO get the user name
          description: "A beautiful landscape", // Add static message
          imageData: base64String
        };

        saveImage(myImage)
          .then((response: SaveImageResponse) => {
            if (response.status === 200) {
              this.dialogOpened = false;
              this.value = response.savedImageId;
              this.loadSignatureImage(response.savedImageId);
              const customEvent = new CustomEvent('image-save-db', {
                detail: {
                  message: 'Image saved successfully. ID',
                  imageID: response.savedImageId
                }
              });
              this.dispatchEvent(customEvent);
              console.log(response.message, response.savedImageId);
            } else {
              console.error('Failed to save image:', response.message);
               this.dialogOpened = false;
            }
          })
          .catch((error) => {
            console.error('Error:', error);
          });
      }
    }
     this.showViewButton = true;
  }

  private registerSignature(component: HTMLElement) {
    console.log(component);
  }

  private base64ToByteArray(base64String: string): Uint8Array {
    const prefix = "data:image";
    if (base64String.startsWith(prefix)) {
      base64String = base64String.slice(base64String.indexOf(',') + 1);
    }
    const binaryString = atob(base64String);
    const length = binaryString.length;
    const bytes = new Uint8Array(length);

    for (let i = 0; i < length; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }

    return bytes;
  }

  private async loadSignatureImage(signID: number) {
    if (signID > 0) {
      getImageById(signID)
        .then(imageData => {
          console.log(imageData);
          const adImageId = imageData.adImageId;
          const binaryData = imageData.binaryData;
          const description = imageData.description;
          const id = imageData.id;
          const name = imageData.name;
          this.shadowRoot.querySelector('vaadin-button').style.display = 'none';

          const img = document.createElement('img');
          img.src = 'data:image/png;base64,' + binaryData;// TODO - handle all image type
          img.alt = description;
          img.width = 200;
          img.height = 150;
          this.shadowRoot.appendChild(img);
             img.addEventListener('click', () => {
                                      this.loadImage(img.src)

                                    });
        })
        .catch(error => {
          console.error("Error fetching image data:", error);
        });
    }
    else {
       if (!signID || signID.length === 0) {
         button.style.display = 'block';
       }
     }
  }

private loadImageIntoSignaturePad(image: string, description: string) {
  const signaturePad = this.shadowRoot.getElementById('signaturePad');
   signaturePad.src = image;

  this.dialogOpened = true;
}


}
