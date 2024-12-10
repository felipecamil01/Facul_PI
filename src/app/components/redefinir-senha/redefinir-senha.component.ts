import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { LoginService } from '../../auth/login.service';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-redefinir-senha',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './redefinir-senha.component.html',
  styleUrl: './redefinir-senha.component.scss'
})
export class RedefinirSenhaComponent {

  email: string = '';
  token: string = '';
  novaSenha: string = '';
  confirmaNovaSenha: string = '';
  loginService = inject(LoginService);
  router = inject(Router);

  constructor(private activatedRoute: ActivatedRoute) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.email = params['email'];
      this.token = params['token'];
    });
  }

  redefinirSenha() {
    if (this.novaSenha !== this.confirmaNovaSenha) {
      Swal.fire('Erro', 'As senhas não coincidem', 'error');
      return;
    }

    this.loginService.redefinirSenha(this.email, this.token, this.novaSenha).subscribe({
      next: () => {
        Swal.fire('Sucesso', 'Senha redefinida', 'success');
        this.router.navigate(['/login']);
      },
      error: (erro) => {
        Swal.fire('Erro', erro.error.message || 'Erro desconhecido', 'error');
      }
    });
  }
}
