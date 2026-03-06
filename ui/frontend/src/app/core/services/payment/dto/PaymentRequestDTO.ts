export interface PaymentRequestDTO {
  idempotencyKey: string;
  bookingId: string;
  amount: number;
}