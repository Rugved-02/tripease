import { Injectable } from '@angular/core';

// Renamed from User to UserCredentials for clarity
export interface UserCredentials {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private userRegistry: UserCredentials[] = [];
  private currentUser:string | null = null;

  /*
   * Validates if the provided credentials exist in the registry.
   */
  checkAuth(inputEmail: string, inputPass: string): boolean {
    console.log("Searching for:", inputEmail);
    console.log("Current registry state:", this.userRegistry);

    const authenticatedAccount = this.userRegistry.find(account => 
      account.email === inputEmail && account.password === inputPass
    );

    if (authenticatedAccount) {
      // Set the class property to the email of the found user
      this.currentUser = authenticatedAccount.email;
      console.log("Login successful for:", this.currentUser);
      return true;
    } else {
      this.currentUser = null; // Clear if login fails
      return false;
    }
  }

  /**
   * Adds a new set of credentials to the internal registry.
   */
  addUser(newAccount: UserCredentials): void {
    this.userRegistry.push(newAccount);
    console.log("User successfully added. Total users:", this.userRegistry.length);
  }

  isLoggedIn(){
    console.log("service is loggedIn");
    if(this.currentUser === null){
      return false;
    }
    return true;
  }
}