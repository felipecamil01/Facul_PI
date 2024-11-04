import { Component, inject, OnInit } from '@angular/core';
import { Cliente } from '../../models/cliente';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule, RouterLink],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.scss'
})
export class ClienteListComponent implements OnInit {
  lista: Cliente[] = []
  listaFiltrada: Cliente[] = []; // Lista filtrada
  listaId: number | null = null; // Para armazenar o ID buscado
  listaNome: string = ''; // Para armazenar o nome buscado


  clienteService = inject(ClienteService);

  constructor(private route:Router){

    this.findAll();
    let cliente: Cliente = new Cliente();
     

  }
  ngOnInit(): void {
    this.findAll(); // Carregar todos os clientes
    this.listaFiltrada = this.lista; // Inicializar listaFiltrada
}

  findAll(){
    this.clienteService.findAll().subscribe({
      next:lista => {
        this.lista = lista;
      },
      error: erro =>   {
        alert("Ocorreu um erro")
      },
    })
  }
  delete(cliente : Cliente){
    Swal.fire({
      title:'Quer deletar este cliente?',
      icon:'warning',
      showConfirmButton:true,
      showDenyButton:true,
      confirmButtonText:'Sim',
      denyButtonText:'Não',
    }).then((result)=>{
      if(result.isConfirmed){
        this.clienteService.delete(cliente.id).subscribe({
          next: lista =>{
            Swal.fire({
              title:'Deletado com sucesso',
              icon: 'success',
              confirmButtonText:'OK'
            })
            this.findAll();
          },
          error: erro =>{
            Swal.fire({
              title:'Erro ao deletar',
              icon: 'error',
              confirmButtonText:'OK'
            })
          }
        })
      }
    }
  )

  }
  salvar(){
    this.route.navigate(['/salvarCliente']);
  }
  editar(id:number){
    this.route.navigate(['/editarCliente',id]);
  }

  filtroClientes() {
    if (!this.listaId && !this.listaNome) {
        // Se nenhum critério de busca estiver definido, mostra todos os clientes
        this.listaFiltrada = this.lista; // Mostrar todos os clientes
    } else {
        // Se há critérios de busca, filtra a lista
        this.listaFiltrada = this.lista.filter(cliente => {
            const matchesId = this.listaId ? cliente.id === this.listaId : true;
            const matchesName = this.listaNome ? cliente.nome.toLowerCase().includes(this.listaNome.toLowerCase()) : true;
            return matchesId && matchesName;
        });
    }
}

  trackById(index: number, cliente: any): number {
    return cliente.id; // Retorna o ID para otimizar o desempenho do *ngFor
  }



    
  }