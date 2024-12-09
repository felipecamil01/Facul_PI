import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { jwtDecode, JwtPayload } from "jwt-decode";
import { Login } from './login';
import { Usuario } from './usuario';
import { environment } from '../../environments/environment'


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  http = inject(HttpClient);
  private API = environment.SERVIDOR + "/api/login";

  constructor() { }

  logar(login: Login): Observable<string> {
    return this.http.post<string>(this.API, login, {responseType: 'text' as 'json'});
  }

  recuperarSenha(email: string): Observable<string> {
    return this.http.post<string>(`${this.API}/recuperar-senha/${email}`, {});
  }


  redefinirSenha(email: string, token: string, novaSenha: string): Observable<void> {
    return this.http.post<void>(
      `${this.API}/savePassword?email=${encodeURIComponent(email)}&token=${encodeURIComponent(token)}&novaSenha=${encodeURIComponent(novaSenha)}`, null
    );
  }
  

  validarToken(email: string, token: string) {
    return this.http.post<boolean>(
      `${this.API}/validar-token?email=${encodeURIComponent(email)}&token=${encodeURIComponent(token)}`, null
    );
  } 

  
  addToken(token: string) {
    localStorage.setItem('token', token);
  }

  removerToken() {
    localStorage.removeItem('token');
  }

  getToken() {
    return localStorage.getItem('token');
  }

  jwtDecode() {
    let token = this.getToken();
    if (token) {
      return jwtDecode<JwtPayload>(token);
    }
    return "";
  }

  hasPermission(role: string) {
    let user = this.jwtDecode() as Usuario;
    if (user.role == role)
      return true;
    else
      return false;
  }
     
  }
