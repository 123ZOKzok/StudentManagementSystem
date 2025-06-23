import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  menuItems = [
    { path: '/dashboard', icon: 'dashboard', title: 'Dashboard' },
    { path: '/data-generation', icon: 'database', title: 'Data Generation' },
    { path: '/data-processing', icon: 'process', title: 'Data Processing' },
    { path: '/file-upload', icon: 'upload', title: 'File Upload' },
    { path: '/student-management', icon: 'people', title: 'Student Management' },
    { path: '/student-report', icon: 'report', title: 'Student Report' }
  ];

  constructor(public authService: AuthService) {}

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get userInitials(): string {
    const username = this.authService.currentUser?.sub || '';
    return username.substring(0, 2).toUpperCase() || 'US';
  }

  get username(): string {
    return this.authService.currentUser?.sub || 'User';
  }

  get userRole(): string {
    const roles = this.authService.currentUser?.roles;
    return roles?.[0] || 'User';
  }
}