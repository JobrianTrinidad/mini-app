
import { html, LitElement } from 'lit';
import { customElement, state } from 'lit/decorators.js';
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
export class Example extends LitElement {

  // Define the dialogOpened property with @state() decorator
  @state()
  private dialogOpened = false;

  private signaturePad: any = null;


  protected override createRenderRoot() {
    const root = super.createRenderRoot();
    // Apply custom theme (only supported if your app uses one)
    applyTheme(root);
    return root;
  }

  protected override render() {
    return html`
          <!-- tag::snippet[] -->
          <vaadin-dialog
            header-title="Signature"
            .opened="${this.dialogOpened}"
            @opened-changed="${(event: DialogOpenedChangedEvent) => {
              this.dialogOpened = event.detail.value;
            }}"
            ${dialogRenderer(this.renderDialog, [])}
            ${dialogFooterRenderer(this.renderFooter, [])}
          ></vaadin-dialog>

          <vaadin-button
            @click="${() => {
              this.dialogOpened = true;
            }}"
          >
            Open Signature Dialog
          </vaadin-button>
          <!-- end::snippet[] -->
        `;
      }

  private renderDialog = () => html`
    <lit-signature-pad
      id="signaturePad"
      @ready="${this.handleSignaturePadReady}"
    ></lit-signature-pad> <!-- Use the LitSignaturePad component here -->
  `;

    private renderFooter = () => html`
      <vaadin-button @click="${this.clearSignature}">Reset</vaadin-button>
      <vaadin-button theme="primary" @click="${this.close}">Save</vaadin-button>
      <vaadin-button theme="primary" @click="${this.close}">Close</vaadin-button>
    `;


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
}
