import { Injectable } from '@angular/core';

export interface User{
  email:string;
  password:string;
}

@Injectable({
  providedIn: 'root',
})


export class AuthService {
          // username:string='user';
          // password:string='password';
          
          private user:User[]=[
            {email:'user@gmail.com',password:'password'},
            {email:'admin@gmail.com',password:'admin@123'},
            {email:'guest@gmail.com',password:'guest@123'}
          ];


          checkAuth(inputemail:string,inputpass:string):boolean{
            // if(this.username==user && this.password==pass){
            //   return true;
            // }
            // else{
            //   return false;
            // }
            console.log("searching for",inputemail,inputpass);
            console.log("In database",this.user);
            return this.user.some(user =>
              user.email === inputemail && user.password === inputpass 
            );
          }

          addUser(newUser: User) {
          this.user.push(newUser);
          console.log("Current user list",this.user);
          }


}
