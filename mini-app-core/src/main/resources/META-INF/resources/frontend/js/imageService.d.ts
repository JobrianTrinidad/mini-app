// frontend/generated/jar-resources/js/imageService.d.ts
declare module 'Frontend/generated/jar-resources/js/imageService' {
import { AxiosResponse } from 'axios';

export interface ImageRequest {
name: string;
description: string;
imageData: string;
}

export interface ImageResponse {
savedImageId: number;
message: string;
status: number;
}

export interface AdImage {
id: number;
name: string;
description: string;
binaryData: string;
adImageId: number;
}

export function getImageById(id: number): Promise<AdImage | null>;

export function saveImage(imageRequest: ImageRequest): Promise<ImageResponse | null>;

export function updateImage(id: number, updatedImage: ImageRequest): Promise<ImageResponse | null>;

export function deleteImage(id: number): Promise<ImageResponse>;
}
