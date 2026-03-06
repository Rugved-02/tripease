// booking.model.ts
export interface BookingRequestDTO {
//   userId: string; it is passed directly from api gateway as header
  resourceId: number;
  resourceType: 'FLIGHT' | 'HOTEL';
  subType: string | null;
  startDate: string; // ISO format: YYYY-MM-DD
  endDate: string | null;
  quantity: number;
  totalAmount: number;
}