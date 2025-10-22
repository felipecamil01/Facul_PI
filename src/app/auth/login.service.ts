import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { Login } from './login';
import { environment } from '../../environments/environment';

interface DecodedToken extends JwtPayload {
  username?: string;
  role?: string;
  id?: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  http = inject(HttpClient);
  private API = environment.SERVIDOR + '/api/login';

  logar(login: Login): Observable<string> {
    return this.http.post<string>(this.API, login, { responseType: 'text' as 'json' });
  }

  recuperarSenha(email: string): Observable<string> {
    return this.http.post<string>(${this.API}/recuperar-senha/, {});
  }

  redefinirSenha(email: string, token: string, novaSenha: string): Observable<void> {
    return this.http.post<void>(
      ${this.API}/savePassword?email=&token=&novaSenha=,
      null
    );
  }

  validarToken(email: string, token: string) {
    return this.http.post<boolean>(
      ${this.API}/validar-token?email=&token=,
      null
    );
  }

  addToken(token: string) {
    localStorage.setItem('token', token);
  }

  removerToken() {
    localStorage.removeItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  jwtDecode(): DecodedToken | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (error) {
      console.error('Erro ao decodificar token JWT', error);
      return null;
    }
  }

  private getCurrentRole(): string | null {
    const payload = this.jwtDecode();
    const role = payload?.role;
    return role ? role.toString().trim().toUpperCase() : null;
  }

  hasPermission(role: string): boolean {
    const targetRole = role?.trim().toUpperCase();
    const currentRole = this.getCurrentRole();
    if (!targetRole || !currentRole) {
      return false;
    }

    if (currentRole === 'ADMIN') {
      return true;
    }

    return currentRole === targetRole;
  }

  isAdmin(): boolean {
    return this.getCurrentRole() === 'ADMIN';
  }

  isAdvogado(): boolean {
    return this.getCurrentRole() === 'ADVOGADO';
  }

  isSecretaria(): boolean {
    return this.getCurrentRole() === 'SECRETARIA';
  }
}
