import { Component } from '@angular/core';
import { MenubarComponent } from '../../../shared/components/menubar.component/menubar.component';
import { ImageModule } from 'primeng/image';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { SearchFlightHotelsHomepageComponent } from '../search-flight-hotels-homepage.component/search-flight-hotels-homepage.component';
import { Footer } from '../../../shared/components/footer/footer';
import { FeaturesPanelHomepageComponent } from '../features-panel-homepage.component/features-panel-homepage.component';

@Component({
  selector: 'app-homepage',
  imports: [MenubarComponent, ImageModule, TagModule, CardModule, SearchFlightHotelsHomepageComponent, Footer, FeaturesPanelHomepageComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class Homepage {

}