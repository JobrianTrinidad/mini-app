import axios, { AxiosResponse } from 'axios';

// Define interfaces
export interface ImageRequest {
    name: string;
    description: string;
    imageData: string;
}

export interface SaveImageResponse {
    adImageId: number;
    message: string;
}

export interface ErrorResponse {
    adImageId: number;
    message: string;
}

// Define functions
export async function getImageById(id: number): Promise<AdImage | null> {
    try {
        // Consider using a more descriptive URL and checking for errors like 404.
        const response: AxiosResponse<AdImage> = await axios.get<AdImage>(`/images/${id}`);
        return response.data;
    } catch (error) {
        // It's good to handle errors properly and provide meaningful messages.
        console.error('Error fetching image by ID:', error);
        return null;
    }
}

export async function saveImage(imageRequest: ImageRequest): Promise<SaveImageResponse | ErrorResponse> {
    try {
        // You might want to use a more descriptive URL for the POST request.
        const response: AxiosResponse<SaveImageResponse> = await axios.post<SaveImageResponse>('/images', imageRequest);
        return response.data;
    } catch (error) {
        // It's good practice to catch errors and provide a fallback response.
        return {
            adImageId: -1,
            message: `Failed to save image: ${(error as Error).message}`
        };
    }
}
