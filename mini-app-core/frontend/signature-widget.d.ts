// frontend/signature-widget.d.ts
declare module 'signature-widget' {
import { LitElement } from 'lit-element';
import SignaturePad from 'signature_pad';

export class LitSignaturePad extends LitElement {
dotSize: number;
minWidth: number;
maxWidth: number;
throttle: number;
minDistance: number;
backgroundColor: string;
penColor: string;
velocityFilterWeight: number;
type: string;
encoderOptions: number;
readOnly: boolean;
img: string;

signaturePad: SignaturePad;
signatureCanvas: HTMLCanvasElement;

initSignaturePad(): void;
clear(): void;
undo(): void;
encodeImage(): void;
getEncodeImage(): string;
dotSizeChanged(): void;
minWidthChanged(): void;
maxWidthChanged(): void;
throttleChanged(): void;
minDistanceChanged(): void;
backgroundColorChanged(): void;
penColorChanged(): void;
velocityFilterWeightChanged(): void;
readOnlyChanged(): void;
imgChanged(): void;
onEncodingChanged(): void;
resizeSignature(): void;
isEmpty(): boolean;
}

customElements.define("lit-signature-pad", LitSignaturePad);
}
