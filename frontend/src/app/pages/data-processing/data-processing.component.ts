import { Component } from '@angular/core';
import { DataService } from '../../services/data.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-data-processing',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './data-processing.component.html',
  styleUrls: ['./data-processing.component.css']
})
export class DataProcessingComponent {
  isLoading = false;
  processedFilePath: string | null = null;
  errorMessage: string | null = null; // ⬅️ Add this

  constructor(
    private dataService: DataService,
    private toastr: ToastrService
  ) {}

  processData() {
    this.isLoading = true;
    this.processedFilePath = null;
    this.errorMessage = null; // Reset error message

    this.dataService.processExcelToCsv().subscribe({
      next: (response: string) => {
        this.toastr.success('CSV file processed successfully!');
        this.processedFilePath = response;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = '❌ Failed to process Excel file. Make sure the latest Excel file exists.';
        this.toastr.error(this.errorMessage);
        this.isLoading = false;
      }
    });
  }
}
