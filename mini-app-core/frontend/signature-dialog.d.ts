// frontend/signature-dialog.d.ts
declare module 'signature-dialog' {
import { LitElement } from 'lit';
import { ImageRequest, ImageResponse } from 'Frontend/generated/jar-resources/js/imageService';
import { DialogOpenedChangedEvent } from '@vaadin/dialog';

export class SignatureDialog extends LitElement {
private dialogOpened: boolean;
private signaturePad: any;

protected createRenderRoot(): ShadowRoot;
protected render(): unknown;

private renderDialog(): unknown;
private renderFooter(): unknown;

private loadImage(imageSrc: string): void;
private openNewSignature(): void;
private open(): void;
private close(): void;
private handleSignaturePadReady(event: Event): void;
private clearSignature(): void;
private saveSignature(): void;
private handleImageResponse(response: ImageResponse): void;
private registerSignature(component: HTMLElement): void;
private base64ToByteArray(base64String: string): Uint8Array;
private loadSignatureImage(signID: number): Promise<void>;
}

export interface ImageRequest {
name: string;
description: string;
imageData: string;
}

export interface ImageResponse {
status: number;
message: string;
savedImageId: number;
}
}
