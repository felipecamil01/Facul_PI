import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Usuario } from '../../auth/usuario';
import { LoginService } from '../../auth/login.service';
import { UsuarioService } from '../../services/usuario-service.service';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-registrar',
  standalone: true,
  imports: [MdbFormsModule,FormsModule],
  templateUrl: './registrar.component.html',
  styleUrl: './registrar.component.scss'
})
export class RegistrarComponent {
  usuario: Usuario = new Usuario();
  
  usuarioService = inject(UsuarioService);
  router = inject(Router);

  registrar() {
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    });

    this.usuarioService.registrar(this.usuario).subscribe({
      next: () => {
        Toast.fire({
          icon: 'success',
          title: 'Usuário cadastrado com sucesso'
        });
        this.router.navigate(['/login']);
      },
      error: erro => {
        Toast.fire({
          icon: 'error',
          title: 'Erro no cadastro',
          text: erro.error
        });
      }
    });
  }
}
