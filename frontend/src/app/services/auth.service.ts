import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

interface DecodedToken {
  sub: string;       // username
  exp: number;
  iat: number;
  roles?: string[];
  // add other claims from your JWT if needed
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<DecodedToken | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // Initialize from token if exists
    const token = this.getToken();
    if (token) {
      const decoded = this.decodeToken(token);
      if (decoded) {
        this.currentUserSubject.next(decoded);
      }
    }
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((response: any) => {
        this.setToken(response.token);
        const decoded = this.decodeToken(response.token);
        if (decoded) {
          this.currentUserSubject.next(decoded);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('access_token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    
    const decoded = this.decodeToken(token);
    if (!decoded) return false;
    
    // Check if token is expired
    return Date.now() < decoded.exp * 1000;
  }

  get currentUser(): DecodedToken | null {
    return this.currentUserSubject.value;
  }

  private setToken(token: string): void {
    localStorage.setItem('access_token', token);
  }

  private getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  private decodeToken(token: string): DecodedToken | null {
    try {
      const payload = token.split('.')[1];
      if (!payload) {
        throw new Error('Invalid token format');
      }
      
      const decoded = JSON.parse(atob(payload));
      if (!decoded.sub) {
        throw new Error('Invalid token content');
      }
      
      return decoded;
    } catch (e) {
      console.error('Error decoding token:', e);
      this.logout(); // Clear invalid token
      return null;
    }
  }
}