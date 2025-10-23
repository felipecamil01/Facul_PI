import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Usuario } from '../../../auth/usuario';
import { UsuarioService } from '../../../services/usuario-service.service';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registrar',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './registrar.component.html',
  styleUrl: './registrar.component.scss'
})
export class RegistrarComponent {
  usuario: Usuario = new Usuario();
  carregando = false;

  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  registrar() {
    if (this.carregando) {
      return;
    }

    if (!this.usuario.username || !this.usuario.email || !this.usuario.password) {
      Swal.fire('Campos obrigatórios', 'Informe usuário, e-mail e senha para continuar.', 'warning');
      return;
    }

    if (!this.usuario.registroToken && localStorage.getItem('token') === null && localStorage.getItem('primeiroAcesso') === null) {
      Swal.fire('Token necessário', 'Informe o token de registro recebido para concluir o cadastro.', 'warning');
      return;
    }

    this.carregando = true;
    this.usuarioService.registrar(this.usuario).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Usuário cadastrado com sucesso',
          confirmButtonText: 'Ir para login'
        }).then(() => this.router.navigate(['/login']));
      },
      error: erro => {
        const mensagem = erro?.error?.message || erro?.error || 'Falha ao efetuar cadastro. Verifique o token informado.';
        Swal.fire('Erro no cadastro', mensagem, 'error');
        this.carregando = false;
      }
    });
  }
}
