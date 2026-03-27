import { Component } from '@angular/core';
import { AnimateOnScrollModule } from 'primeng/animateonscroll';
import { AvatarModule } from 'primeng/avatar';


@Component({
  selector: 'app-about-us',
  imports: [AnimateOnScrollModule, AvatarModule],
  templateUrl: './about-us.html',
  styleUrl: './about-us.css',
})
export class AboutUs {

}