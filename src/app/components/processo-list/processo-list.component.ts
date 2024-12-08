import { Component, inject, OnInit } from '@angular/core';
import { Processo } from '../../models/processo.model';
import { ProcessoService } from '../../services/processo.service';
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-processo-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './processo-list.component.html',
  styleUrl: './processo-list.component.scss'
})
export class ProcessoListComponent implements OnInit {
  lista: Processo[] = [];
  listaFiltrada: Processo[] = []; 

  processoService = inject(ProcessoService);

  constructor(private route: Router) {}

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.processoService.findAll().subscribe({
      next: (lista) => {
        this.lista = this.lista;
      },
      error: (erro) => {
        console.error('Erro ao carregar processos:', erro);
      },
    });
  }

  deletarProcesso(processo: Processo) {
    Swal.fire({
      title: 'Deseja deletar este processo?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then((result) => {
      if (result.isConfirmed) {
        this.processoService.delete(processo.id).subscribe({
          next: () => {
            Swal.fire('Deletado com sucesso!', '', 'success');
            this.findAll();
          },
          error: (erro) => {
            Swal.fire('Erro ao deletar o processo!', '', 'error');
            console.error(erro);
          },
        });
      }
    });
  }
}