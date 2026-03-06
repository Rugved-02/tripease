import { ResourceDetailsDTO } from "./ResourceDetailsDTO";


export interface RecentBookingsResponseDTO {
  resourceType: 'HOTEL' | 'FLIGHT'; // Using string literal types for safety
  subType: string;
  bookingStatus: string;
  startDate: string;
  endDate?: string; // Optional for flights
  totalAmount: number;
  createdAt: string;
  updatedAt: string;
  resourceDetails: ResourceDetailsDTO;
}