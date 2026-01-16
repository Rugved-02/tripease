import { Component,ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DrawerModule } from 'primeng/drawer';
import { Drawer } from 'primeng/drawer';
import {RippleModule} from 'primeng/ripple';
import {AvatarModule} from 'primeng/avatar';
import {StyleClassModule} from 'primeng/styleclass';

@Component({
  selector: 'app-dashboard.component',
  imports: [DrawerModule,DrawerModule,     
    ButtonModule,     
    RippleModule,      
    AvatarModule,      
    StyleClassModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
      @ViewChild('drawerRef') drawerRef!: Drawer;

      visible: boolean = false;



      closeCallback(event: Event): void {
      this.drawerRef.close(event);
  }

}
