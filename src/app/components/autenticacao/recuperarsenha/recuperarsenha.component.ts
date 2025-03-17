import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { LoginService } from '../../../auth/login.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-recuperar-senha',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './recuperarsenha.component.html',
  styleUrl: './recuperarsenha.component.scss'
})
export class RecuperarSenhaComponent {

  email: string = '';
  token: string = '';
  etapa: 'email' | 'token' = 'email';

  loginService = inject(LoginService);
  router = inject(Router);

  recuperarSenha() {
    const Toast = Swal.mixin({});

    this.loginService.recuperarSenha(this.email).subscribe({
      next: () => {
        Toast.fire({
          icon: 'success',
          title: 'E-mail de recuperação enviado com sucesso'
        });
        this.etapa = 'token';
      },
      error: () => {
        Toast.fire({
          icon: 'error',
          title: 'Erro na recuperação',
          text: 'E-mail não encontrado'
        });
      }
    });
  }

  validarToken() {
    this.loginService.validarToken(this.email, this.token).subscribe({
      next: (valido) => {
        if (valido) {
          Swal.fire('Sucesso', 'Token validado com sucesso!', 'success');
          this.router.navigate(['/redefinir-senha'], { queryParams: { email: this.email, token: this.token } });
        } else {
          Swal.fire('Token inválido', 'Por favor, verifique o token', 'error');
        }
      },
      error: () => {
        Swal.fire('Erro', 'Token inválido', 'error');
      }
    });
  }
}
