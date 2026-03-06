import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, forkJoin, of, switchMap } from 'rxjs';
import { environment } from '../../../../environments/environment.development';

export interface Trip {
  id?: number;
  tripName: string;
  startDate: string; // ISO Format: YYYY-MM-DD
  endDate: string;   // ISO Format: YYYY-MM-DD
  activities?: any[];
}

@Injectable({ providedIn: 'root' })
export class ItineraryService {

    private http = inject(HttpClient);

  private readonly API_URL = `${environment.gatewayUrl}/itinerary`;

  constructor() {}

  getMyTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${this.API_URL}/trips/my-trips`);
  }

  getActivities(tripId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/activities/trip/${tripId}`);
  }

/**
   * Creates a trip first, then uses the returned ID to create activities.
   * This matches your ActivityRequestDTO requirement for a tripId.
   */
    saveFullTrip(tripData: Trip, activities: any[]): Observable<any> {
      return this.http.post<any>(`${this.API_URL}/trips`, tripData).pipe(
        switchMap((savedTrip) => {
          // Use tripId (matches your backend model)
          const id = savedTrip.tripId || savedTrip.id; 

          if (!activities?.length) return of(savedTrip);

          const requests = activities.map(act => 
            this.http.post(`${this.API_URL}/activities`, { ...act, tripId: id })
          );

          return forkJoin(requests);
        })
      );
    }
  // PUT: Update trip metadata
  updateTrip(tripId: number, tripData: Trip): Observable<Trip> {
    return this.http.post<Trip>(`${this.API_URL}/trips/${tripId}`, tripData);
  }

  // DELETE: This assumes you might add a delete endpoint to your TripController later
  deleteTrip(tripId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/trips/${tripId}`);
  }
}