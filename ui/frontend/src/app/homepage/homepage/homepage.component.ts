import { Component } from '@angular/core';
import { MenubarComponent } from '../../shared/components/menubar/menubar.component';
import { ImageModule } from 'primeng/image';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { SearchFlightHotelsHomepageComponent } from "../search-flight-hotels-homepage/search-flight-hotels-homepage.component";
import { FooterComponent } from "../../shared/components/footer/footer.component";
import { FeaturesPanelHomepageComponent } from "../features-panel-homepage/features-panel-homepage.component";

@Component({
  selector: 'app-homepage',
  imports: [MenubarComponent, ImageModule, TagModule, CardModule, SearchFlightHotelsHomepageComponent, FooterComponent, FeaturesPanelHomepageComponent],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css',
})
export class Homepage {

}
