import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Agenda } from '../../../models/agenda.model';
import { AgendaService } from '../../../services/agenda.service';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink,RouterModule],
  templateUrl: './agenda-list.component.html',
  styleUrl: './agenda-list.component.scss',
})
export class AgendaListComponent implements OnInit {
  loginService = inject(LoginService);
  lista: Agenda[] = [];
  listaFiltrada: Agenda[] = []; 

  agendaService = inject(AgendaService);

  constructor(private route: Router) {}

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.agendaService.findAll().subscribe({
      next: (lista) => {
        this.lista = lista;
      },
      error: (erro) => {
        console.log('Ocorreu um erro:', erro);
      },
    });
  }

  delete(agenda: Agenda) {
    Swal.fire({
      title: 'Quer deletar esta agenda?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then((result) => {
      if (result.isConfirmed) {
        this.agendaService.delete(agenda.id).subscribe({
          next: () => {
            Swal.fire({
              title: 'Deletado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK',
            });
            this.findAll();
          },
          error: (erro) => {
            Swal.fire({
              title: 'Erro ao deletar',
              icon: 'error',
              confirmButtonText: 'OK',
            });
          },
        });
      }
    });
  }
  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}
