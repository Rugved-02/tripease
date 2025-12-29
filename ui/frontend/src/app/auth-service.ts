import { Injectable } from '@angular/core';

export interface User{
  username:string;
  password:string;
}

@Injectable({
  providedIn: 'root',
})


export class AuthService {
          // username:string='user';
          // password:string='password';
          
          private user:User[]=[
            {username:'user',password:'password'},
            {username:'admin',password:'admin@123'},
            {username:'guest',password:'guest@123'}
          ];


          checkAuth(inputuser:string,inputpass:string):boolean{
            // if(this.username==user && this.password==pass){
            //   return true;
            // }
            // else{
            //   return false;
            // }
            console.log("searching for",inputuser,inputpass);
            console.log("In database",this.user);
            return this.user.some(user =>
              user.username === inputuser && user.password === inputpass 
            );
          }

          addUser(newUser: User) {
          this.user.push(newUser);
          console.log("Current user list",this.user);
          }
}
