import { Component } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  studentCount = 0;

  constructor(private studentService: StudentService) {
    this.loadStudentCount();
  }

  loadStudentCount() {
    this.studentService.getStudentCount().subscribe({
      next: (count: number) => this.studentCount = count,
      error: (err) => console.error('Failed to load student count:', err)
    });
  }
}
