import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student.model';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-student-report',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-report.component.html',
  styleUrls: ['./student-report.component.css']
})
export class StudentReportComponent implements OnInit {
  students: Student[] = [];
  currentPage = 1;
  itemsPerPage = 10;
  collectionSize = 0;
  isLoading = false;

  // Filters
  searchTerm = '';
  selectedClass = '';
  startDate = '';
  endDate = '';

  // Modal/Action states
  selectedStudent: Student | null = null;
  deleteConfirmStudent: Student | null = null;

  constructor(
    private studentService: StudentService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    this.isLoading = true;

    const params: any = {
      page: this.currentPage - 1,
      size: this.itemsPerPage
    };

    if (this.searchTerm) params.studentId = this.searchTerm;
    if (this.selectedClass) params.class = this.selectedClass;
if (this.startDate) {
  const fromDate = new Date(this.startDate);
  params.fromDob = fromDate.toISOString().split('T')[0]; // Format: yyyy-MM-dd
}
if (this.endDate) {
  const toDate = new Date(this.endDate);
  params.toDob = toDate.toISOString().split('T')[0];
}


    this.studentService.getStudentsWithFilters(params).subscribe({
      next: (res: any) => {
        this.students = res.content;
        this.collectionSize = res.totalElements;
        this.isLoading = false;
      },
      error: () => {
        this.toastr.error('Failed to load students');
        this.isLoading = false;
      }
    });
  }

  exportToExcel() {
    this.studentService.exportStudentsToExcel().subscribe({
      next: (blob: Blob) => {
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = 'students_report.xlsx';
        a.click();
        URL.revokeObjectURL(objectUrl);
      },
      error: () => this.toastr.error('Failed to export report')
    });
  }

  viewStudent(student: Student) {
    this.selectedStudent = student;
  }

  editStudent(student: Student) {
    this.toastr.info(`Edit feature not yet implemented for ID: ${student.studentId}`);
  }

  confirmDelete(student: Student) {
    this.deleteConfirmStudent = student;
  }

  deleteStudent() {
    if (!this.deleteConfirmStudent) return;

    this.studentService.softDeleteStudent(this.deleteConfirmStudent.studentId).subscribe({
      next: () => {
        this.toastr.success('Student deleted successfully');
        this.loadStudents();
        this.deleteConfirmStudent = null;
      },
      error: () => {
        this.toastr.error('Failed to delete student');
      }
    });
  }

  // Pagination Utilities

  getStartIndex(): number {
    return (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  getEndIndex(): number {
    const end = this.currentPage * this.itemsPerPage;
    return end > this.collectionSize ? this.collectionSize : end;
  }

  get totalPages(): number {
    return Math.ceil(this.collectionSize / this.itemsPerPage);
  }

  getPaginationPages(): (number | string)[] {
    const pages: (number | string)[] = [];
    const total = this.totalPages;
    const current = this.currentPage;
    const range = 2;

    if (total > 0) pages.push(1);
    let start = Math.max(2, current - range);
    if (start > 2) pages.push('...');

    for (let i = start; i < current; i++) {
      pages.push(i);
    }

    if (current !== 1 && current !== total) {
      pages.push(current);
    }

    let end = Math.min(total - 1, current + range);
    for (let i = current + 1; i <= end; i++) {
      pages.push(i);
    }

    if (end < total - 1) pages.push('...');
    if (total !== 1 && total !== 0) pages.push(total);

    return pages;
  }

  changePage(page: number | string) {
    if (typeof page === 'number' && page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadStudents();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadStudents();
    }
  }

  nextPage() {
    const totalPages = Math.ceil(this.collectionSize / this.itemsPerPage);
    if (this.currentPage < totalPages) {
      this.currentPage++;
      this.loadStudents();
    }
  }
}
