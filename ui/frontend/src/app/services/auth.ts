import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private USER_KEY = 'tripease_user';
  private SESSION_KEY = 'tripease_session';

  constructor() {}

  // Save user registration data
  register(userData: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(userData));
  }

  // Retrieve the registered user object
  getUser() {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  /**
   * Validates login credentials.
   * Renamed from 'login' to 'checkAuth' to match your Login Component call.
   */
  checkAuth(email: string, pass: string): boolean {
    const savedUser = this.getUser();
    
    if (savedUser && savedUser.email === email && savedUser.password === pass) {
      // Optional: Mark the user as "logged in" for this browser session
      sessionStorage.setItem(this.SESSION_KEY, 'true');
      return true;
    }
    return false;
  }

  // Helper to check if a user is currently logged in (useful for Route Guards)
  isLoggedIn(): boolean {
    return sessionStorage.getItem(this.SESSION_KEY) === 'true';
  }

  // Clear session on logout
  logout(): void {
    sessionStorage.removeItem(this.SESSION_KEY);
  }
}