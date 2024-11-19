import { Component, inject, OnInit } from '@angular/core';
import { Cliente } from '../../models/cliente.model';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, Subject } from 'rxjs';

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

  private searchSubject = new Subject<string>();
  clienteService = inject(ClienteService);

  constructor(private route: Router) {}

  ngOnInit(): void {
    this.findAll();
    this.setupSearch();
  }

  findAll() {
    this.clienteService.findAll().subscribe({
      next: (lista) => {
        this.lista = lista;
        this.listaFiltrada = lista;
      },
      error: (erro) => console.log('Ocorreu um erro:', erro),
    });
  }

  setupSearch() {
    this.searchSubject.pipe(debounceTime(300)).subscribe((nome) => {
      if (nome.length >= 3) {
        this.clienteService.findByNome(nome).subscribe({
          next: (listaFiltrada) => (this.listaFiltrada = listaFiltrada),
          error: (erro) => console.log('Erro ao buscar clientes:', erro),
        });
      } else {
        this.listaFiltrada = this.lista;
      }
    });
  }

  filtroClientes() {
    if (!this.listaId && this.listaNome.length < 3) {
      this.listaFiltrada = this.lista;
    } else if (this.listaNome.length >= 3) {
      this.searchSubject.next(this.listaNome);
    } else {
      this.listaFiltrada = this.lista.filter((cliente) => cliente.id === this.listaId);
    }
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
          next: () => {
            Swal.fire('Deletado com sucesso', '', 'success');
            this.findAll();
          },
          error: () => Swal.fire('Erro ao deletar', '', 'error'),
        });
      }
    });
  }

<<<<<<< HEAD
  salvar() {
    this.route.navigate(['admin/salvarCliente']);
  }

  editar(id: number) {
    this.route.navigate(['admin/editarCliente', id]);
  }

  acentosNoFiltro(input: string): string {
    return input
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase();
  }

  trackById(index: number, cliente: any): number {
=======
  trackById(index: number, cliente: Cliente): number {
>>>>>>> 8a42c293f6c2fb982b62ff62249d2dfb5ffb0bc4
    return cliente.id;
  }
}
