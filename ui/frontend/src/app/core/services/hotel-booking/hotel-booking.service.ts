import { Injectable } from '@angular/core';

export interface Hotel {
  id:string;
  name: string;
  location: string;
  image: string;
  price: number;
  rating: number;
  stars: number;
  reviews: number;
  roomsLeft: number;
}

@Injectable({
  providedIn: 'root',
})
export class HotelBookingService {
  
 private hotels = [
  { id: 'HT101', name: 'Grand Plaza Hotel', location: 'Los Angeles, USA', image: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=800&q=80', price: 250, rating: 4.8, stars: 5, reviews: 1245, roomsLeft: 15 },
  { id: 'HT102', name: 'The Ritz-Carlton', location: 'Dubai, UAE', image: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80', price: 450, rating: 4.9, stars: 5, reviews: 3210, roomsLeft: 5 },
  { id: 'HT103', name: 'Blue Lagoon Resort', location: 'Maldives', image: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=800&q=80', price: 600, rating: 5.0, stars: 5, reviews: 980, roomsLeft: 2 },
  { id: 'HT104', name: 'Mountain Peak Lodge', location: 'Aspen, USA', image: 'https://images.unsplash.com/photo-1518733057094-95b53143d2a7?auto=format&fit=crop&w=800&q=80', price: 310, rating: 4.7, stars: 4, reviews: 750, roomsLeft: 10 },
  { id: 'HT105', name: 'London Bridge Hotel', location: 'London, UK', image: 'https://images.unsplash.com/photo-1535827841776-24afc1e255ac?auto=format&fit=crop&w=800&q=80', price: 280, rating: 4.6, stars: 4, reviews: 2100, roomsLeft: 8 },
  { id: 'HT106', name: 'Tokyo Skyline Suites', location: 'Tokyo, Japan', image: 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80', price: 350, rating: 4.9, stars: 5, reviews: 1500, roomsLeft: 12 },
  { id: 'HT107', name: 'Parisian Elegance', location: 'Paris, France', image: 'https://images.unsplash.com/photo-1499092346589-b9b6be3e94b2?auto=format&fit=crop&w=800&q=80', price: 400, rating: 4.8, stars: 5, reviews: 1850, roomsLeft: 4 },
  { id: 'HT108', name: 'Sydney Harbour Inn', location: 'Sydney, Australia', image: 'https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?auto=format&fit=crop&w=800&q=80', price: 220, rating: 4.5, stars: 4, reviews: 900, roomsLeft: 20 },
  { id: 'HT109', name: 'Venice Canal Palace', location: 'Venice, Italy', image: 'https://images.unsplash.com/photo-1527631746610-bca00a040d60?auto=format&fit=crop&w=800&q=80', price: 320, rating: 4.7, stars: 4, reviews: 1100, roomsLeft: 6 },
  { id: 'HT110', name: 'Swiss Alps Retreat', location: 'Zermatt, Switzerland', image: 'https://images.unsplash.com/photo-1502784444187-359ac186c5bb?auto=format&fit=crop&w=800&q=80', price: 550, rating: 5.0, stars: 5, reviews: 600, roomsLeft: 3 },
  { id: 'HT111', name: 'Marina Bay Sands', location: 'Singapore', image: 'https://images.unsplash.com/photo-1529516548873-9ce57c8f155e?auto=format&fit=crop&w=800&q=80', price: 500, rating: 4.9, stars: 5, reviews: 5400, roomsLeft: 25 },
  { id: 'HT112', name: 'New York Central Hotel', location: 'New York, USA', image: 'https://images.unsplash.com/photo-1496417263034-38ec4f0b665a?auto=format&fit=crop&w=800&q=80', price: 380, rating: 4.6, stars: 4, reviews: 3300, roomsLeft: 14 },
  { id: 'HT113', name: 'Santorini Sunset', location: 'Santorini, Greece', image: 'https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?auto=format&fit=crop&w=800&q=80', price: 420, rating: 4.8, stars: 5, reviews: 1400, roomsLeft: 1 },
  { id: 'HT114', name: 'Berlin City Hub', location: 'Berlin, Germany', image: 'https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?auto=format&fit=crop&w=800&q=80', price: 190, rating: 4.4, stars: 3, reviews: 850, roomsLeft: 18 },
  { id: 'HT115', name: 'Rome Heritage Inn', location: 'Rome, Italy', image: 'https://images.unsplash.com/photo-1552832230-c0197dd311b5?auto=format&fit=crop&w=800&q=80', price: 260, rating: 4.7, stars: 4, reviews: 2200, roomsLeft: 9 },
  { id: 'HT116', name: 'Cape Town View', location: 'Cape Town, South Africa', image: 'https://images.unsplash.com/photo-1580619305218-8423a7ef79b4?auto=format&fit=crop&w=800&q=80', price: 210, rating: 4.5, stars: 4, reviews: 720, roomsLeft: 11 },
  { id: 'HT117', name: 'Bangkok Riverside', location: 'Bangkok, Thailand', image: 'https://images.unsplash.com/photo-1563911302283-d2bc129e7570?auto=format&fit=crop&w=800&q=80', price: 150, rating: 4.3, stars: 3, reviews: 1600, roomsLeft: 30 },
  { id: 'HT118', name: 'Dubai Desert Oasis', location: 'Dubai, UAE', image: 'https://images.unsplash.com/photo-1451153378752-16ef2b36ad05?auto=format&fit=crop&w=800&q=60', price: 390, rating: 4.8, stars: 5, reviews: 2100, roomsLeft: 7 },
  { id: 'HT119', name: 'Barcelona Beachside', location: 'Barcelona, Spain', image: 'https://images.unsplash.com/photo-1583037189850-1921ae7c6c22?auto=format&fit=crop&w=800&q=80', price: 290, rating: 4.6, stars: 4, reviews: 1350, roomsLeft: 13 },
  { id: 'HT120', name: 'Mumbai Grand', location: 'Mumbai, India', image: 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=800&q=80', price: 180, rating: 4.5, stars: 4, reviews: 1100, roomsLeft: 22 }
];

getHotels(): Hotel[] {
    return this.hotels;
  }
}
