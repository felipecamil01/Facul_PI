import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AgendaService } from '../../services/agenda';
import { Contato } from '../../models/agenda.model';
import { ClienteService } from '../../services/cliente.service';

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
      alert('Por favor, selecione um cliente.');
      return;
    }
    this.contato.clienteId = this.selectedClienteId;
    console.log('Contato a ser salvo:', this.contato);

    if (this.editando) {
      console.log(this.contato.clienteId);
      this.agendaService.updateContato(this.contato).subscribe(
        () => {
          this.carregarContatos();
          this.limparFormulario();
        },
        (error) => {
          console.error('Erro ao atualizar contato:', error);
        }
      );
    } else {
      this.agendaService.registrarContato(this.contato).subscribe(
        () => {
          this.carregarContatos();
          this.limparFormulario();
        },
        (error) => {
          console.error('Erro ao criar contato:', error);
        }
      );
    }
  }

  editarContato(contato: Contato) {
    this.selectedClienteId = contato.cliente!.id;
    this.contato = { ...contato };
    this.editando = true;
  }

  excluirContato(id: number | undefined) {
    if (
      id !== undefined &&
      confirm('Tem certeza que deseja excluir este contato?')
    ) {
      this.agendaService.deleteContato(id).subscribe(
        () => {
          this.carregarContatos();
        },
        (error) => {
          console.error('Erro ao excluir contato:', error);
        }
      );
    }
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
