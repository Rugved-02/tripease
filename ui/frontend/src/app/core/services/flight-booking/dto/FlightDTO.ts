export interface FlightDTO {
  id?: number;
  airline: string;
  flightNo: string;
  class: string;
  depTime: string;
  depCity: string;
  arrTime: string;
  arrCity: string;
  duration: string;
  price: number;
  seats: number;
}