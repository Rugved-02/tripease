import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';

// Renamed from User to UserCredentials for clarity
export interface UserCredentials {
  email: string;
  password: string;
}

export interface UserCredentialsRegister {
  email: string;
  password: string;
  name: string;
  mobile: string;
}
@Injectable({
  providedIn: 'root',
})
export class AuthService {

  // private userRegistry: UserCredentials[] = [];
  // private currentUser:string | null = null;
  private http = inject(HttpClient);
private readonly API_URL = `${environment.gatewayUrl}/auth`;



/**
   * Hits the Spring Boot /auth/login endpoint.
   * Spring Boot returns a JWT token if successful.
   */
  checkAuth(inputEmail: string, inputPass: string): Observable<boolean> {
    const encodedEmail = btoa(inputEmail);
    const encodedPassword = btoa(inputPass);
    const loginData = { email: encodedEmail, password: encodedPassword };


    return this.http.post<any>(`${this.API_URL}/login`, loginData).pipe(
      tap((response) => {
        // Assuming your backend returns { token: '...' }
        if (response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', inputEmail);
        }
      }),
      map(() => true), // If the request succeeds, return true
      catchError((error) => {
        console.error('Login failed:', error);
        this.logout(); // Clear storage on error
        return of(false); // Return false to the component
      })
    );
  }



  /**
   * Hits the Spring Boot /auth/register endpoint.
   */
  registerUser(newAccount: UserCredentialsRegister): Observable<any> {
      // 1. Convert the entire object to a JSON string
    const jsonString = JSON.stringify(newAccount);

    // 2. Encode that string to Base64
    const base64Encoded = btoa(jsonString);

    // 3. Send it to the backend inside a "payload" or "data" key
    const requestBody = { data: base64Encoded };

    return this.http.post(`${this.API_URL}/register`, requestBody);
  }


  /**
   * Checks if a token exists in local storage.
   */
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return token !== null;
  }


  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
  }


}
 