import { LitElement, CSSResult } from 'lit-element';
import { DialogRenderer, DialogFooterRenderer } from '@vaadin/dialog';
import { ImageRequest, ImageResponse } from 'Frontend/generated/jar-resources/js/imageService';

export declare class CameraComponent extends LitElement {
value: number;
grid: any;
rowKey: string;
columnInfo: any;
fileIds: any[];
fileMap: Map<any, any>;

static styles: CSSResult;

constructor();

render(): import("lit-element").TemplateResult;

firstUpdated(): void;

registerCamera(component: HTMLElement): void;

private loadImageFile(imageIds: number[]): Promise<void>;

private findFileId(fileToFind: any): any;
}

export declare class MemoryBuffer {
inputStream: ReadableStream<Uint8Array>;

constructor(inputStream: ReadableStream<Uint8Array>);
}

export declare class RoundButton {
text: string;
diameter: string;

constructor(text: string, diameter: string);

getElement(): HTMLButtonElement;
}

export declare class Upload {
buffer: MemoryBuffer;
acceptedFileTypes: string;
maxFileSize: number;

constructor(buffer: MemoryBuffer);

getElement(): HTMLInputElement;
}

export declare class FileDialog extends LitElement {
dialogOpened: boolean;
fileMap: Map<any, any>;

constructor();

render(): import("lit-element").TemplateResult;

private renderDialog: () => import("lit-element").TemplateResult;

private renderFooter: () => import("lit-element").TemplateResult;

handleClose(): void;

deleteFile(event: any, id: any): void;

private open(): void;

private close(): void;
}

declare global {
interface HTMLElementTagNameMap {
'camera-component': CameraComponent;
'file-dialog': FileDialog;
}
}

export declare const dialogRenderer: DialogRenderer;
export declare const dialogFooterRenderer: DialogFooterRenderer;

export declare function getImageById(imageId: number): Promise<any>;
export declare function saveImage(image: ImageRequest): Promise<ImageResponse>;
export declare function updateImage(image: ImageRequest): Promise<ImageResponse>;
export declare function deleteImage(imageId: number): Promise<ImageResponse>;
