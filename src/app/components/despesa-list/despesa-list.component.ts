import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Despesa } from '../../models/despesa.model';
import { DespesaService } from '../../services/despesa.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-despesa-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './despesa-list.component.html',
  styleUrl: './despesa-list.component.scss',
})
export class DespesaListComponent implements OnInit {
  lista: Despesa[] = [];
  listaFiltrada: Despesa[] = []; 
  listaId: number | null = null; 
  listaNome: string = ''; 

  despesaService = inject(DespesaService);

  constructor(private route: Router) {
    let despesa: Despesa = new Despesa();
  }
  ngOnInit(): void {
    this.findAll();
    this.listaFiltrada = this.lista;
  }

  findAll() {
    this.despesaService.findAll().subscribe({
      next: (lista) => {
        this.lista = lista;
      },
      error: (erro) => {
        console.log('Ocorreu um erro:', erro);
      },
    });
  }

  delete(despesa: Despesa) {
    Swal.fire({
      title: 'Quer deletar esta despesa?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then((result) => {
      if (result.isConfirmed) {
        this.despesaService.delete(despesa.id).subscribe({
          next: (lista) => {
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
  salvar() {
    this.route.navigate(['/salvarDespesa']);
  }
  editar(id: number) {
    this.route.navigate(['/editarDespesa', id]);
  }

}
