import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { LoginService } from '../../auth/login.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-recuperarsenha',
  standalone: true,
  imports: [MdbFormsModule,FormsModule],
  templateUrl: './recuperarsenha.component.html',
  styleUrl: './recuperarsenha.component.scss'
})
export class RecuperarSenhaComponent {
  email: string = '';
  token:string='';
  novaSenha:string='';
  etapa:'email'|'token'='email';
  
  loginService = inject (LoginService);
  router = inject(Router);

  recuperarSenha() {
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    });

    this.loginService.recuperarSenha(this.email).subscribe({
      next: () => {
        Toast.fire({
          icon: 'success',
          title: 'Token enviado para seu email'
        });
      },
      error: erro => {
        Toast.fire({
          icon: 'error',
          title: 'Erro na recuperação',
          text: erro.error
        });
      }
    });
  }
}