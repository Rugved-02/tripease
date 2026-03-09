export interface UserResponseDTO {
  userId: string;    // Matches UUID
  name: string;
  email: string;
  mobile: string;
  createdAt: string; // ISO 8601 string format (e.g., "2026-03-09T11:57:22")
}