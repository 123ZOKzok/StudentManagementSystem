import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../app/components/layout/header/header.component';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from "./components/layout/sidebar/sidebar.component";
import { LoadingSpinnerComponent } from "./components/shared/loading-spinner/loading-spinner.component";
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeaderComponent,
    SidebarComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  loading = false;

  constructor(public authService: AuthService) {}
}