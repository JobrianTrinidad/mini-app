import { html, LitElement } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import { ImageRequest, ImageResponse, getImageById, saveImage, updateImage, deleteImage } from 'Frontend/generated/jar-resources/js/imageService.tsx';
import { ifDefined } from 'lit/directives/if-defined.js';
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
export class SignatureDialog extends LitElement {

  @state()
  private dialogOpened = false;

  private signaturePad: any = null;

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
       @click="${this.openNewSignature}"
      >
     Signature
      </vaadin-button>
      <img src="./" style="display: none;" />
      `;
  }

  private renderDialog = () => html`
    <lit-signature-pad
      id="signaturePad"
      @ready="${this.handleSignaturePadReady}"
      .img=${this.imageSrc}
    >
    </lit-signature-pad>
  `;

  private renderFooter = () => html`
    <vaadin-button @click="${this.clearSignature}">Reset</vaadin-button>
    <vaadin-button theme="primary" @click="${this.saveSignature}">Save</vaadin-button>
    <vaadin-button theme="primary" @click="${this.close}">Close</vaadin-button>
  `;

  private loadImage(imageSrc: string) {
    this.imageSrc = imageSrc;
    this.dialogOpened = true
  }

  private openNewSignature(){
    this.imageSrc =null;
    this.clearSignature();
    this.dialogOpened = true;
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
    if (this.signaturePadInitialized && this.signaturePad) {
      this.signaturePad.clear();
    }
  }

  private saveSignature() {
    if (this.signaturePadInitialized && this.signaturePad) {
      const id = this.value !== undefined ? this.value : -1;
      if(this.signaturePad.isEmpty() && id <= 0)
      {
        return;
      }
      else if(this.signaturePad.isEmpty() &&  id > 0)
      {
           deleteImage(id)
            .then((response: ImageResponse) => {
               this.handleImageResponse(response);
            })
            .catch((error) => {
              console.error('Error:', error);
            });
      }
      else
      {
          let base64String = this.signaturePad.getEncodeImage();
          const prefix = "data:image";
          if (base64String.startsWith(prefix)) {
            base64String = base64String.slice(base64String.indexOf(',') + 1);
          }

          const myImage: ImageRequest = {
            name: "Signature",
            description: "Signature",
            imageData: base64String
          };

          if(id <= 0)
          {
            saveImage(myImage)
              .then((response: ImageResponse) => {
                 this.handleImageResponse(response);
              })
              .catch((error) => {
                console.error('Error:', error);
              });
          }
          else
          {
            updateImage(id, myImage)
              .then((response: ImageResponse) => {
                this.handleImageResponse(response);
              })
              .catch((error) => {
                console.error('Error:', error);
              });
          }
        }
    }
  }

  private handleImageResponse(response: ImageResponse) {
    if (response.status === 200) {
      this.dialogOpened = false;
      this.value = response.savedImageId;
      this.loadSignatureImage(this.value);
      const customEvent = new CustomEvent('image-save-db', {
        detail: {
          message: response.message,
          imageID: response.savedImageId
        }
      });
      this.dispatchEvent(customEvent);
    } else {
      console.error('Failed to save image:', response.message);
      this.dialogOpened = false;
    }
  }

  private registerSignature(component: HTMLElement) {
//     console.log(component); // TODO- if need to call the server method
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
          const adImageId = imageData.adImageId;
          const binaryData = imageData.binaryData;
          const description = imageData.description;
          const id = imageData.id;
          const name = imageData.name;
          this.value = adImageId;
          this.shadowRoot.querySelector('vaadin-button').style.display = 'none';

          const img = this.shadowRoot.querySelector('img');
          img.src = 'data:image/png;base64,' + binaryData; // TODO - handle all image type
          img.alt = description;
          img.style = "height:37px; width: 90%;";
          this.shadowRoot.appendChild(img);
          img.addEventListener('click', () => this.loadImage(img.src));
        })
        .catch(error => {
          console.error("Error fetching image data:", error);
        });
    }
    else if(this.shadowRoot)
    {
      this.shadowRoot.querySelector('vaadin-button').style.display = "";
      const img = this.shadowRoot.querySelector('img');
      img.src = "";
      img.alt = "";
      img.style.display = "none";
    }
  }
}
