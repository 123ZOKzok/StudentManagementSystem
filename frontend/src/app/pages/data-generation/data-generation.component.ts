import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-data-generation',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './data-generation.component.html',
  styleUrls: ['./data-generation.component.css']
})
export class DataGenerationComponent {
  recordCount: number | null = null;
  isLoading: boolean = false;

  constructor(private http: HttpClient) {}

  generateData() {
    if (!this.recordCount || this.recordCount < 1) {
      alert('Please enter a valid number of records.');
      return;
    }

    this.isLoading = true;

    this.http.post(`http://localhost:8080/api/data-generation/generate-students?recordCount=${this.recordCount}`, null, {
      responseType: 'blob'
    }).subscribe({
      next: (blob) => {
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = `students_${Date.now()}.xlsx`;
        a.click();
        URL.revokeObjectURL(objectUrl);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error generating file:', error);
        alert('Failed to generate file. Check backend.');
        this.isLoading = false;
      }
    });
  }
}
