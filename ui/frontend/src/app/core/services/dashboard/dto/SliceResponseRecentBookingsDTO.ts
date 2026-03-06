export interface SliceResponseRecentBookingsDTO<T> {
  content: T[];
  last: boolean;    // True if there are no more pages
  first: boolean;
  number: number;  // Current page index
  size: number;    // Page size
}