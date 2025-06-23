import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './pages/login/login.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      },
      {
        path: 'data-generation',
        loadComponent: () => import('./pages/data-generation/data-generation.component')
          .then(m => m.DataGenerationComponent)
      },
      {
        path: 'data-processing',
        loadComponent: () => import('./pages/data-processing/data-processing.component')
          .then(m => m.DataProcessingComponent)
      },
      {
        path: 'file-upload',
        loadComponent: () => import('./pages/file-upload/file-upload.component')
          .then(m => m.FileUploadComponent)
      },
      {
        path: 'student-management',
        loadComponent: () => import('./pages/student-management/student-management.component')
          .then(m => m.StudentManagementComponent)
      },
      {
        path: 'student-report',
        loadComponent: () => import('./pages/student-report/student-report.component')
          .then(m => m.StudentReportComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
