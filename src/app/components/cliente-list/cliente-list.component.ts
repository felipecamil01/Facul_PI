import { Component, inject, OnInit } from '@angular/core';
import { Cliente } from '../../models/cliente.model';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.scss',
})
export class ClienteListComponent implements OnInit {
  lista: Cliente[] = [];
  listaFiltrada: Cliente[] = []; 
  listaId: number | null = null; 
  listaNome: string = ''; 

  clienteService = inject(ClienteService);

  constructor(private route: Router) {
    let cliente: Cliente = new Cliente();
  }
  ngOnInit(): void {
    this.findAll();
    this.listaFiltrada = this.lista;
  }

  findAll() {
    this.clienteService.findAll().subscribe({
      next: (lista) => {
        this.lista = lista;
      },
      error: (erro) => {
        console.log('Ocorreu um erro:', erro);
      },
    });
  }
  delete(cliente: Cliente) {
    Swal.fire({
      title: 'Quer deletar este cliente?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then((result) => {
      if (result.isConfirmed) {
        this.clienteService.delete(cliente.id).subscribe({
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
    this.route.navigate(['/salvarCliente']);
  }
  editar(id: number) {
    this.route.navigate(['/editarCliente', id]);
  }

  acentosNoFiltro(input: string): string {
    return input
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase();
  }

  filtroClientes() {
    if (!this.listaId && !this.listaNome) {
      this.listaFiltrada = this.lista;
    } else {
      this.listaFiltrada = this.lista.filter((cliente) => {
        const matchesId = this.listaId ? cliente.id === this.listaId : true;
        const matchesName =
          this.listaNome && this.listaNome.length >= 3
            ? this.acentosNoFiltro(cliente.nome).includes(
                this.acentosNoFiltro(this.listaNome)
              )
            : true;
        return matchesId && matchesName;
      });
    }
  }

  trackById(index: number, cliente: any): number {
    return cliente.id; 
  }
}
