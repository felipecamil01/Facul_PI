import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../auth/usuario';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/login';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  registrar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/criar`, usuario);
  }

  buscarPorUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/usuarios/${username}`);
  }

  atualizarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/usuarios`, usuario);
  }

  alterarSenha(dadosSenha: {
    username: string, 
    senhaAtual: string, 
    novaSenha: string
  }): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/alterar-senha`, dadosSenha);
  }
}
