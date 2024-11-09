import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AgendaService } from '../../services/agenda';
import { Contato } from '../../models/agenda.model';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-agenda',
  templateUrl: './agenda.component.html',
  styleUrls: ['./agenda.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class AgendaComponent implements OnInit {
  contatos: Contato[] = [];
  clientes: any[] = [];

  selectedClienteId: number | null = null;
  contato: Contato = {
    clienteId: null,
    dataUltimoContato: '',
    meioContato: '',
    notasContato: '',
    proximoPassos: '',
  };
  editando = false;
  today!: string;

  constructor(
    private agendaService: AgendaService,
    private clienteService: ClienteService
  ) {}

  ngOnInit() {
    this.today = new Date().toISOString().split('T')[0];
    this.carregarContatos();
    this.carregaCliente();
  }
  carregaCliente() {
    this.clienteService.findAll().subscribe(
      (data) => (this.clientes = data),
      (error) => console.error('Erro ao buscar clientes', error)
    );
  }

  getNomeCliente(clienteId: number | null): string {
    console.log('ID do cliente solicitado:', clienteId);
    if (clienteId === null) {
      return 'Cliente não especificado';
    }
    console.log('Clientes disponíveis:', this.clientes);
    const cliente = this.clientes.find((c) => c.id === clienteId);
    return cliente ? cliente.nome : 'Cliente não encontrado';
  }

  carregarContatos() {
    this.agendaService.getContatos().subscribe(
      (data) => {
        this.contatos = data;
        console.log('Contatos carregados:', this.contato);
      },
      (error) => {
        console.error('Erro ao carregar contatos:', error);
      }
    );
  }

  salvarContato() {
    if (this.selectedClienteId === null) {
      Swal.fire({
        title:'Por Favor, selecione um cliente.',
        icon:'warning',
        confirmButtonText:'OK'
      })
      return;
    }
    this.contato.clienteId = this.selectedClienteId;
    console.log('Contato a ser salvo:', this.contato);

    if (this.editando) {
      console.log(this.contato.clienteId);
      this.agendaService.updateContato(this.contato).subscribe(
        () => {
          Swal.fire({
            title:'Atualizado com sucesso',
            icon:'success',
            confirmButtonText:'OK'
          })
          this.carregarContatos();
          this.limparFormulario();
        },
        (error) => {
          Swal.fire({
            title:'Erro ao Atualizar',
            icon: 'error',
            confirmButtonText:'OK'
          })
        }
      );
    } else {
      this.agendaService.registrarContato(this.contato).subscribe(
        () => {
          this.carregarContatos();
          this.limparFormulario();
        },
        (error) => {
          Swal.fire({
            title:'Erro ao salvar',
            icon: 'error',
            confirmButtonText:'OK'
          })
        }
      );
    }
  }

  editarContato(contato: Contato) {
    this.selectedClienteId = contato.cliente!.id;
    this.contato = { ...contato };
    this.editando = true;
  }
  excluirContato(id:number){
    Swal.fire({
      title:'Quer deletar este evento?',
      icon:'warning',
      showConfirmButton:true,
      showDenyButton:true,
      confirmButtonText:'Sim',
      denyButtonText:'Não',
    }).then((result)=>{
      if(result.isConfirmed){
        this.agendaService.deleteContato(id).subscribe({
          next: lista =>{
            Swal.fire({
              title:'Deletado com sucesso',
              icon: 'success',
              confirmButtonText:'OK'
            })
            this.carregarContatos();
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

  limparFormulario() {
    this.contato = {
      clienteId: null,
      dataUltimoContato: '',
      meioContato: '',
      notasContato: '',
      proximoPassos: '',
    };
    this.selectedClienteId = null;
    this.editando = false;
  }
}
