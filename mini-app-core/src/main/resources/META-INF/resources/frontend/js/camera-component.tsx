import { customElement, property, state } from 'lit/decorators.js';
import { LitElement, html, css } from 'lit';
import { dialogFooterRenderer, dialogRenderer } from '@vaadin/dialog/lit.js';
import { Dialog } from '@vaadin/dialog';
import { ImageRequest, ImageResponse, getImageById, saveImage, deleteImage } from 'Frontend/generated/jar-resources/js/imageService.tsx';

// Extending HTMLElement to include fileMap property
interface HTMLElement {
  fileMap?: Map<number, { name: string; fileData: string }>;
}

interface HTMLElementEventMap {
  'file-delete': CustomEvent<{ imageID: number }>;
  'close-dialog': CustomEvent<void>;
}

@customElement('camera-component')
class CameraComponent extends LitElement {
    @property({ type: Number }) value = 0;
    @property({ type: Object }) grid = null;
    @property({ type: String }) rowKey = null;
    @property({ type: Object }) columnInfo = null;
    @property({ type: Array }) fileIds: number[] = [];
    @property({ type: Map }) fileMap = new Map<number, { name: string; fileData: string }>();

    static styles = css`
        vaadin-upload-file-list {
            display: none;
        }

        camera-component {
            width: 100%;
            display: flex;
        }

        vaadin-button {
            color: white !important;
            background-color: rgb(50, 111, 112) !important;
        }
    `;

    constructor() {
        super();
    }

    render() {
        return html`
            <vaadin-horizontal-layout>
            </vaadin-horizontal-layout>
        `;
    }

    firstUpdated() {
        const buffer = new MemoryBuffer(new ReadableStream<Uint8Array>());
        const upload = new Upload(buffer);
        const roundButton = new RoundButton(this.fileMap.size.toString(), '50px');
        const roundButtonElement = roundButton.getElement();
        this.addEventListener('update-button-count', () => {
            roundButtonElement.textContent = this.fileMap.size.toString();
        });
        roundButtonElement.addEventListener('click', () => {
            const dialog = document.createElement('file-dialog') as FileDialog;
            dialog.fileMap = this.fileMap;
            document.body.appendChild(dialog);
            dialog.addEventListener('close-dialog', () => {
                document.body.removeChild(dialog);
                roundButtonElement.textContent = this.fileMap.size.toString();
            });

            dialog.addEventListener('file-delete', (event) => {
                const customEvent = event as CustomEvent<{ imageID: number }>;
                const fileIdToDelete = customEvent.detail.imageID;
                if (typeof fileIdToDelete === 'number' && fileIdToDelete > 0) {
                    deleteImage(fileIdToDelete)
                        .then((response: ImageResponse | null) => {
                         if (response !== null) {
                                this.fileIds = this.fileIds.filter(id => id !== fileIdToDelete);
                                this.fileMap.delete(fileIdToDelete);
                                const imageSaveEvent = new CustomEvent('image-save-db', {
                                    detail: {
                                        message: response.message,
                                        imageID: this.fileIds
                                    }
                                });
                                this.dispatchEvent(imageSaveEvent);
                            }
                        })
                        .catch((error) => {
                            console.error('Error:', error);
                        });
                }
            });
            dialog.open();
        });

        this.style.display = 'flex';
        // Get the upload input element
        const uploadInput = upload.getElement();
        uploadInput.style.width = "80%";

        // Apply styles to uploadInput
        uploadInput.style.padding = '5px';

        uploadInput.addEventListener('upload-error', (event) => {
            const customEvent = event as CustomEvent;
            if (customEvent.detail != null && customEvent.detail.xhr != null && customEvent.detail.xhr.responseText != null) {
                console.log('Upload Error', customEvent);
            }
        });

        uploadInput.addEventListener('upload-cancel', (event) => {
            const customEvent = event as CustomEvent;
            if (customEvent.detail != null && customEvent.detail.xhr != null && customEvent.detail.xhr.responseText != null) {
                console.log('Upload Cancel', JSON.parse(customEvent.detail.xhr.responseText).message);
            }
        });

        uploadInput.addEventListener('upload-success', (event) => {
            const customEvent = event as CustomEvent;
            const file = customEvent.detail.file;

            const reader = new FileReader();
            reader.onload = (e) => {
                const mimeType = file.type;
                const fileName = file.name;
                const fileData = reader.result as string;
                let base64String = fileData;
                const prefix = "data:image";
                if (base64String.startsWith(prefix)) {
                    base64String = base64String.slice(base64String.indexOf(',') + 1);
                }

                const myImage: ImageRequest = {
                    name: fileName,
                    description: 'Camera Image ' + fileName,
                    imageData: base64String
                };

                saveImage(myImage)
                    .then((response: ImageResponse | null) => {
                        if (response !== null) {
                            if (response.status === 200) {
                                this.fileMap.set(response.savedImageId, { name: fileName, fileData: base64String });
                                this.fileIds = [...this.fileIds, response.savedImageId];
                                const imageSaveEvent = new CustomEvent('image-save-db', {
                                    detail: {
                                        message: response.message,
                                        imageID: this.fileIds
                                    }
                                });
                                this.dispatchEvent(imageSaveEvent);
                                this.dispatchEvent(new CustomEvent('update-button-count'));
                                // Clear only the successfully uploaded file from the vaadin-upload-file-list
                                const fileList = uploadInput.querySelector('vaadin-upload-file-list') as HTMLInputElement;
                                if (fileList) {
                                    const fileItem = Array.from(fileList.items).find(item => item === file);
                                    if (fileItem) {
                                        if (fileList.items) {
                                            fileList.items.delete(fileItem);
                                        }
                                    }
                                }
                            } else {
                                console.error('Failed to save image:', response.message);
                            }
                         } else {
                            console.error('Failed to save image: No response');
                        }
                    })
                    .catch((error) => {
                        console.error('Error:', error);
                    });
            };
            reader.readAsDataURL(file); // Read file as data URL

        });
        this.shadowRoot.appendChild(uploadInput);
        this.shadowRoot.appendChild(roundButtonElement);
    }

    registerCamera(component: HTMLElement) {
        // console.log(component); // TODO- if need to call the server method
    }

    private async loadImageFile(imageIds: number[]) {
        if (imageIds !== null && imageIds.length > 0) {
            for (const imageId of imageIds) {
                if (imageId > 0) {
                    try {
                        const imageData = await getImageById(imageId);
                        if (imageData) {
                            const adImageId = imageData.adImageId;
                            const binaryData = imageData.binaryData;
                            const description = imageData.description;
                            const id = imageData.id;
                            const fileName = imageData.name;
                            this.fileMap.set(id, { name: fileName, fileData: binaryData });
                            this.fileIds = [...this.fileIds, id];
                            this.dispatchEvent(new CustomEvent('update-button-count'));
                        }
                    } catch (error) {
                        console.error("Error fetching image data:", (error as Error).message);
                    }
                }
            }
        }
    }

    // Function to find the ID of a file
    private findFileId(fileToFind: { name: string; fileData: string }): number | null {
        for (const [id, file] of this.fileMap) {
            if (file === fileToFind) {
                return id;
            }
        }
        return null; // File not found
    }
}

// Define a class to represent MemoryBuffer
class MemoryBuffer {
    inputStream: ReadableStream<Uint8Array>;

    constructor(inputStream: ReadableStream<Uint8Array>) {
        this.inputStream = inputStream;
    }
}

// Define a class to represent a custom round button
class RoundButton {
    text: string;
    diameter: string;

    constructor(text: string, diameter: string) {
        this.text = text;
        this.diameter = diameter;
    }

    // Getter method for element
    getElement(): HTMLButtonElement {
        const button = document.createElement('vaadin-button');
        button.textContent = this.text;
        button.style.width = this.diameter;
        button.style.height = this.diameter;
        button.style.borderRadius = '50%';
        button.style.border = 'none';
        button.style.cursor = 'pointer';
        button.style.padding = '5px';
        return button;
    }
}

// Define a class to represent Upload
class Upload {
    buffer: MemoryBuffer;
    acceptedFileTypes: string;
    maxFileSize: number;

    constructor(buffer: MemoryBuffer) {
        this.buffer = buffer;
        this.acceptedFileTypes = 'image/*';
        this.maxFileSize = 10485760; // 10 MB
    }

    // Getter method for element
    getElement(): HTMLInputElement {
        const input = document.createElement('vaadin-upload');
        input.type = 'file';
        input.accept = this.acceptedFileTypes;
        return input;
    }
}

@customElement('file-dialog')
class FileDialog extends LitElement {
    @state()
    private dialogOpened: boolean = false;

    @property({ type: Map })
    fileMap: Map<number, { name: string; fileData: string }> = new Map();
    static get properties() {
        return {
            fileMap: {
                type: Map
            }
        };
    }

    constructor() {
        super();
        this.fileMap = new Map();
    }

    render() {
        return html`
      <vaadin-dialog
        header-title="Upload File"
        .opened="${this.dialogOpened}"
        @opened-changed="${(event: CustomEvent) => {
            const customEvent = event as CustomEvent<{ value: boolean }>;
            this.dialogOpened = customEvent.detail.value;
        }}"
        .cancel="${true}"
        @cancel="${this.handleClose}"
        ${dialogRenderer<any>(() => this.renderDialog(), [])}
        ${dialogFooterRenderer<any>(() => this.renderFooter(), [])}
      ></vaadin-dialog>
    `;
    }

    private renderDialog = () => html`
    ${Array.from(this.fileMap).map(([id, file]) => html`
      <div data-file-id="${id}" style="display: flex; justify-content: space-between; align-items: center; padding: 10px;">
        <span style="padding-right: 10px;">${file.name}</span>
        <button @click="${(event: Event) => this.deleteFile(event, id)}" style="background-color: transparent; border: none; align-self: flex-end; cursor: pointer">X</button>
      </div>
    `)}
  `;

    private renderFooter = () => html`
    <vaadin-button theme="primary" @click="${this.handleClose}">Close</vaadin-button>
  `;

    handleClose() {
        this.dispatchEvent(new CustomEvent('close-dialog'));
        this.close();
    }

    deleteFile(event: Event, id: number) {
        this.fileMap.delete(id);
        const divToRemove = (event.target as HTMLElement).parentElement;
        if (divToRemove) {
            divToRemove.remove();
        }
        const customEvent = new CustomEvent('file-delete', {
            detail: {
                imageID: id
            }
        } as CustomEventInit<{ imageID: number }>);
        this.dispatchEvent(customEvent);
    }

    public open() {
        this.dialogOpened = true;
    }

    public close() {
        this.dialogOpened = false;
    }
}
