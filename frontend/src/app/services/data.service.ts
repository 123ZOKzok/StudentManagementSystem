import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private generationUrl = 'http://localhost:8080/api/data-generation';
  private processingUrl = 'http://localhost:8080/api/data-processing';

  constructor(private http: HttpClient) {}

  // Generates Excel student data and returns a blob (for download)
  generateStudentData(recordCount: number): Observable<Blob> {
    return this.http.post(`${this.generationUrl}/generate-students`, null, {
      params: { recordCount: recordCount.toString() },
      responseType: 'blob'
    });
  }

  // Processes the most recent Excel file (no filename required)
  processExcelToCsv(): Observable<string> {
    return this.http.post(`${this.processingUrl}/process-excel`, {}, {
      responseType: 'text'
    });
  }
}
