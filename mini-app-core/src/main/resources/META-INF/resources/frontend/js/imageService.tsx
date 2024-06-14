import axios, { AxiosResponse } from 'axios';

// Define interfaces
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
    adImageId: number;
    name: string;
    binaryData: Uint8Array | ArrayBuffer | null; // Use appropriate type for binary data
    description?: string; // Optional property
}

// Helper function to get the base URL
function getBaseUrl(): string {
    return window.contextPath ? `${window.contextPath}/images` : '/images';
}

// Define functions
export async function getImageById(id: number): Promise<AdImage | null> {
    try {
        const response: AxiosResponse<AdImage> = await axios.get<AdImage>(`${getBaseUrl()}/${id}`);
        return response.data;
    } catch (error) {
        // It's good to handle errors properly and provide meaningful messages.
        console.error('Error fetching image by ID:', error);
        return null;
    }
}

export async function saveImage(imageRequest: ImageRequest): Promise<ImageResponse | null> {
    try {
        const response: AxiosResponse<ImageResponse> = await axios.post<ImageResponse>(getBaseUrl(), imageRequest);
        return response.data;
    } catch (error) {
        return {
            savedImageId: -1,
            message: `Failed to save image: ${(error as Error).message}`,
            status: -1
        };
    }
}

export async function updateImage(id: number, updatedImage: ImageRequest): Promise<ImageResponse | null> {
    try {
        const response: AxiosResponse<ImageResponse> = await axios.put<ImageResponse>(`${getBaseUrl()}/${id}`, updatedImage);
        return response.data;
    } catch (error) {
        return {
            savedImageId: -1,
            message: `Failed to update image: ${(error as Error).message}`,
            status: -1
        };
    }
}

export async function deleteImage(id: number): Promise<ImageResponse> {
    try {
        const response: AxiosResponse<ImageResponse> = await axios.delete(`${getBaseUrl()}/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error deleting image:', error);
        return {
            savedImageId: -1,
            message: `Failed to delete image: ${(error as Error).message}`,
            status: -1
        };
    }
}