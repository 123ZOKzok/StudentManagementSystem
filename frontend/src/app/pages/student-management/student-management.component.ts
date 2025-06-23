import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from '../../components/shared/confirm-dialog/confirm-dialog.component';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student.model';

@Component({
  selector: 'app-student-management',
  standalone: true,
  templateUrl: './student-management.component.html',
  styleUrls: ['./student-management.component.css'],
  imports: [CommonModule, FormsModule, ConfirmDialogComponent]
})
export class StudentManagementComponent implements OnInit {
  students: Student[] = [];
  allStudents: Student[] = [];

  currentPage = 1;
  itemsPerPage = 10;
  collectionSize = 0;
  isLoading = false;
  searchTerm = '';
  sortColumn: string | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';

  showConfirmDialog = false;
  studentToDelete: number | null = null;

  constructor(
    private studentService: StudentService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    this.isLoading = true;
    this.studentService.getStudents(this.currentPage - 1, this.itemsPerPage).subscribe({
      next: (res: any) => {
        this.allStudents = res.content;
        this.collectionSize = res.totalElements;
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => {
        this.toastr.error('Failed to load students');
        this.isLoading = false;
      }
    });
  }

  applyFilter() {
    let filtered = [...this.allStudents];

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(s =>
        s.firstName?.toLowerCase().includes(term) ||
        s.lastName?.toLowerCase().includes(term) ||
        s.studentId?.toString().includes(term) ||
        s.studentClass?.toLowerCase().includes(term)
      );
    }

    if (this.sortColumn) {
      filtered.sort((a: any, b: any) => {
        const valA = a[this.sortColumn!];
        const valB = b[this.sortColumn!];
        return this.sortDirection === 'asc'
          ? valA > valB ? 1 : -1
          : valA < valB ? 1 : -1;
      });
    }

    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    this.students = filtered.slice(startIndex, startIndex + this.itemsPerPage);
    this.collectionSize = filtered.length;
  }

  sort(column: string) {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.applyFilter();
  }

  confirmDelete(id: number) {
    this.studentToDelete = id;
    this.showConfirmDialog = true;
  }

  handleConfirmation(confirmed: boolean) {
    if (confirmed && this.studentToDelete !== null) {
      this.studentService.softDeleteStudent(this.studentToDelete).subscribe({
        next: () => {
          this.toastr.success('Student deleted');
          this.loadStudents();
        },
        error: () => this.toastr.error('Failed to delete')
      });
    }
    this.studentToDelete = null;
    this.showConfirmDialog = false;
  }

  getStartIndex(): number {
    return (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  getEndIndex(): number {
    return Math.min(this.currentPage * this.itemsPerPage, this.collectionSize);
  }

  onItemsPerPageChange() {
    this.currentPage = 1;
    this.applyFilter();
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

    for (let i = start; i < current; i++) pages.push(i);
    if (current !== 1 && current !== total) pages.push(current);

    let end = Math.min(total - 1, current + range);
    for (let i = current + 1; i <= end; i++) pages.push(i);
    if (end < total - 1) pages.push('...');
    if (total > 1) pages.push(total);

    return pages;
  }

  changePage(page: number | string) {
    if (typeof page === 'number') {
      this.currentPage = page;
      this.applyFilter();
    }
  }

  editStudent(student: Student) {
    this.toastr.info('Edit not yet implemented');
  }
}
