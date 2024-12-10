import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { LoginService } from '../../../auth/login.service';
import { Login } from '../../../auth/login';
import { Usuario } from '../../../auth/usuario';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MdbFormsModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  login:Login=new Login();
  

  loginService = inject(LoginService);

  router = inject(Router);

  constructor(){
    this.loginService.removerToken();
  }
  autenticar() {
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.onmouseenter = Swal.stopTimer;
        toast.onmouseleave = Swal.resumeTimer;
      }
    });
  }
  
    logar() {
      this.loginService.logar(this.login).subscribe({
        next: token => {
          if(token)
            this.loginService.addToken(token);
          this.router.navigate(['/admin/dashboard']);
        },
        error: erro => {
          const Toast= Swal.fire({
            icon: 'error',
            title: 'Falha no login',
            text: 'Credenciais inválidas ou erro no servidor'
          });
          console.error(erro);
        }
      });
    }
  
}
