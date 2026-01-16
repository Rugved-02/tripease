import { Injectable } from '@angular/core';

export interface User {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})


export class Auth {


  private user: User[] = [ ];


  checkAuth(inputemail: string, inputpass: string): boolean {
    console.log("searching for", inputemail, inputpass);
    console.log("In database", this.user);
    return this.user.some(user =>
      user.email === inputemail && user.password === inputpass
    );
  }

  addUser(newUser: User) {
    this.user.push(newUser);
    console.log("Current user list", this.user);
  }

}
 