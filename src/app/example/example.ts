import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OnInit,OnChanges,OnDestroy } from '@angular/core';
import { Child } from './child/child';
@Component({
  selector: 'app-example',
  imports: [FormsModule,Child],
  templateUrl: './example.html',
  styleUrl: './example.css',
})
export class Example {
abc="supriya";
msg="";
username:string="peter";
showdata(){
  this.msg="hello world";

}
ngOnInit(){
  console.log("oninit");
}
ngOnChanges(){
  console.log("onchanges");
}
ngOnDestroy(){
  console.log("ondestroy");
}

}
