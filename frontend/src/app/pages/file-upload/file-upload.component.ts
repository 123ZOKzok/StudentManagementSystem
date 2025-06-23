import { Component } from '@angular/core';
import { FileUploadService } from '../../services/file-upload.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  isLoading = false;

  constructor(
    private uploadService: FileUploadService,
    private toastr: ToastrService
  ) {}

onFileSelected(event: Event) {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    const file = input.files[0];
    const allowedTypes = [
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', // .xlsx
      'application/vnd.ms-excel' // .xls
    ];

    if (!allowedTypes.includes(file.type)) {
      this.toastr.error('Invalid file type. Please upload an Excel file (.xls or .xlsx).');
      this.selectedFile = null;
      return;
    }

    this.selectedFile = file;
  }
}


  uploadFile(): void {
    if (!this.selectedFile) return;

    this.isLoading = true;
    this.uploadService.uploadStudents(this.selectedFile).subscribe({
      next: (response: any) => {
        this.toastr.success(`Processed ${response.processedCount} students`);
        this.selectedFile = null;
        this.isLoading = false;
      },
      error: (err: any) => {
        this.toastr.error(err.error?.message || 'Error uploading file');
        this.isLoading = false;
      }
    });
  }
}
