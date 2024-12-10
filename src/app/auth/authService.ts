import { HttpClient } from "@angular/common/http";
import { inject, Inject, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";

@Injectable({
    providedIn: 'root',
  })
  export class AuthService {
    http= inject (HttpClient);
    A
    
    private userRole: string = '';
  
    login(credentials: any): Observable<any> {
      return this.http.post('', credentials).pipe(
        tap((response: any) => {
          localStorage.setItem('token', response.token);
          this.userRole = response.role; // Salva o papel do usuário
        })
      );
    }
  
    getRole(): string {
      return this.userRole || localStorage.getItem('role') || 'user';
    }
  
    isAdmin(): boolean {
      return this.getRole() === 'admin';
    }
  }
  