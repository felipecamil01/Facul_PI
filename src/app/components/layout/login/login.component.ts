import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  router = inject(Router);

  usuario!: string;
  senha!: string;

  logar() {
    if(this.usuario == 'admin' && this.senha == 'admin'){
      this.router.navigate(['/principal/clientes']);
    }else{
      alert('Nome do usuário ou senha incorreto. Tente novamente.')
    }
  }
}
