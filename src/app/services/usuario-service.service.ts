import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../auth/usuario';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = environment.SERVIDOR + '/api/login';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  registrar(usuario: Usuario): Observable<Usuario> {
    const payload = {
      username: usuario.username,
      email: usuario.email,
      password: usuario.password,
      token: usuario.registroToken
    };
    return this.http.post<Usuario>(${this.apiUrl}/criar, payload);
  }

  buscarPorUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(${this.apiUrl}/usuarios/);
  }

  atualizarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(${this.apiUrl}/usuarios, usuario);
  }

  alterarSenha(dadosSenha: {
    username: string,
    senhaAtual: string,
    novaSenha: string
  }): Observable<void> {
    return this.http.post<void>(${this.apiUrl}/alterar-senha, dadosSenha);
  }
}
