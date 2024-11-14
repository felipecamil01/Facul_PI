import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, registerLocaleData } from '@angular/common';
import Swal from 'sweetalert2';
import localePt from '@angular/common/locales/pt';
import { ClienteService } from '../../services/cliente.service';
import { AgendaService } from '../../services/agenda.service';
import { Agenda } from '../../models/agenda.model';
import { StatusPagamento } from '../../models/status-pagamento.enum';
import { Router, ActivatedRoute } from '@angular/router';

registerLocaleData(localePt, 'pt-BR');

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './agenda-form.component.html',
  styleUrls: ['./agenda-form.component.scss']
})

export class AgendaFormComponent implements OnInit {
  agenda: Agenda[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  selectedClienteId: number = 0;
  clientes: any[] = [];
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  statusOptions = Object.values(StatusPagamento);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private agendaService: AgendaService,
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {
    this.form = this.fb.group({
      clienteId: ['', [Validators.required]],
      meioContato: ['', [Validators.required]],
      dataUltimoContato: ['', [Validators.required]],
      notasContato: ['', [Validators.required]],
      proximoPassos: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.carregarRegistros();
    this.carregaCliente();

    if (this.route.snapshot.paramMap.get('id')) {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      this.agendaService.findById(id).subscribe({
        next: agenda => this.editarRegistro(agenda),
        error: err => console.log(err)
      });
    }
  }

  carregarRegistros(): void {
    this.agendaService.findAll().subscribe({
      next: data => this.agenda = data,
      error: error => console.error('Erro ao carregar registros:', error)
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      if (this.modoEdicao && this.registroSelecionadoId) {
        const dadosParaSalvar = { ...this.form.value };
        this.agendaService.update(this.registroSelecionadoId, dadosParaSalvar).subscribe({
          next: () => {
            Swal.fire({ title: 'Atualizado com sucesso', icon: 'success', confirmButtonText: 'OK' });
            this.carregarRegistros();
            this.limparFormulario();
            this.router.navigate(['principal/agendas']);
          },
          error: error => Swal.fire({ title: 'Erro ao Atualizar', icon: 'error', confirmButtonText: 'OK' })
        });
      } else {
        this.clienteService.findById(this.form.value.clienteId).subscribe({
          next: cliente => {
            const dadosParaSalvar = { ...this.form.value, cliente: cliente };
            this.agendaService.save(dadosParaSalvar).subscribe({
              next: () => {
                Swal.fire({ title: 'Cadastrado com sucesso', icon: 'success', confirmButtonText: 'OK' });
                this.carregarRegistros();
                this.limparFormulario();
                this.router.navigate(['principal/agendas']);
              },
              error: error => Swal.fire({ title: 'Erro ao salvar', icon: 'error', confirmButtonText: 'OK' })
            });
          },
          error: error => Swal.fire({ title: 'Erro ao buscar cliente', icon: 'error', confirmButtonText: 'OK' })
        });
      }
    }
  }

  editarRegistro(agenda: Agenda): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = agenda.id;
    this.form.patchValue({
      clienteId: agenda.cliente.id,
      meioContato: agenda.meioContato,
      dataUltimoContato: agenda.dataUltimoContato,
      notasContato: agenda.notasContato,
      proximoPassos: agenda.proximoPassos
    });
  }

  excluirRegistro(id: number): void {
    Swal.fire({
      title: 'Quer deletar esta agenda?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then(result => {
      if (result.isConfirmed) {
        this.agendaService.delete(id).subscribe({
          next: () => {
            Swal.fire({ title: 'Deletado com sucesso', icon: 'success', confirmButtonText: 'OK' });
            this.carregarRegistros();
          },
          error: error => Swal.fire({ title: 'Erro ao deletar', icon: 'error', confirmButtonText: 'OK' })
        });
      }
    });
  }

  limparFormulario(): void {
    this.form.reset();
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
  }

  carregaCliente(): void {
    this.clienteService.findAll().subscribe(
      data => this.clientes = data,
      error => console.error('Erro ao buscar clientes', error)
    );
  }
}
