import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AgendaService } from '../../../services/agenda.service';
import { RouterModule } from '@angular/router';
import { LoginService } from '../../../auth/login.service';
import { ProcessoService } from '../../../services/processo.service';
import { ClienteService } from '../../../services/cliente.service';
import { ClienteDTO } from '../../../models/ClienteDTO';

@Component({
  selector: 'app-agenda-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './agenda-form.component.html',
  styleUrls: ['./agenda-form.component.scss']
})
export class AgendaFormComponent implements OnInit {
  loginService = inject(LoginService);
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  processos: any[] = [];
  clientes: ClienteDTO[] = [];

  @Input() eventoParaEditar?: any;
  @Input() selectedDate!: Date;
  @Output() close = new EventEmitter<void>();

  constructor(
    private agendaService: AgendaService,
    private fb: FormBuilder,
    private processoService: ProcessoService,
    private clienteService: ClienteService
  ) {
    this.form = this.fb.group({
      titulo: ['', [Validators.required]],
      descricao: [''],
      tipo: ['Processo', [Validators.required]],
      horario: ['09:00', [Validators.required]],
      processoId: [null],
      clienteId: [null],
      prazoImportante: [false],
      prioridade: ['NORMAL']
    });
  }

  ngOnInit(): void {
    this.carregarProcessos();
    this.carregarClientes();

    this.form.get('processoId')?.valueChanges.subscribe((processoId) => {
      if (processoId) {
        const processoSelecionado = this.processos.find((p) => p.id === processoId);
        if (processoSelecionado?.cliente?.id) {
          this.form.patchValue({ clienteId: processoSelecionado.cliente.id }, { emitEvent: false });
        }
      }
    });

    if (this.eventoParaEditar) {
      this.carregarDadosParaEdicao(this.eventoParaEditar);
    } else if (this.selectedDate) {
      this.carregarDadosParaNovoEvento(this.selectedDate);
    }
  }

  private carregarProcessos(): void {
    this.processoService.findAll().subscribe({
      next: (lista) => (this.processos = lista),
      error: () => console.error('Não foi possível carregar processos')
    });
  }

  private carregarClientes(): void {
    this.clienteService.findAll().subscribe({
      next: (lista) => (this.clientes = lista),
      error: () => console.error('Não foi possível carregar clientes')
    });
  }

  private carregarDadosParaEdicao(evento: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = evento.id;
    const dataEvento = new Date(evento.data);

    this.form.patchValue({
      titulo: evento.titulo || evento.descricao || '',
      descricao: evento.descricao || '',
      tipo: evento.tipo && evento.tipo !== 'PRAZO_IMPORTANTE' ? evento.tipo : 'Processo',
      horario: dataEvento.toTimeString().slice(0, 5),
      processoId: evento.processo?.id || null,
      clienteId: evento.cliente?.id || evento.processo?.cliente?.id || null,
      prazoImportante: !!evento.prazoImportante || (evento.tipo && evento.tipo === 'PRAZO_IMPORTANTE'),
      prioridade: evento.prioridade || 'NORMAL'
    });
  }

  private carregarDadosParaNovoEvento(date: Date): void {
    this.modoEdicao = false;
    this.selectedDate = date;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      Swal.fire('Atenção!', 'Verifique os campos obrigatórios antes de salvar.', 'warning');
      return;
    }

    const clienteId = this.form.value.clienteId;
    const processoId = this.form.value.processoId;
    const [h, m] = this.form.value.horario.split(':');
    const dataFinal = new Date(this.selectedDate);
    dataFinal.setHours(parseInt(h, 10));
    dataFinal.setMinutes(parseInt(m, 10));

    const tipoSelecionado = this.form.value.prazoImportante ? 'PRAZO_IMPORTANTE' : this.form.value.tipo;
    const payload: any = {
      descricao: this.form.value.descricao || this.form.value.titulo,
      tipo: tipoSelecionado,
      data: dataFinal.toISOString(),
      prazoImportante: !!this.form.value.prazoImportante,
      prioridade: this.form.value.prioridade,
      processo: processoId ? { id: processoId } : undefined,
      cliente: clienteId ? { id: clienteId } : undefined
    };

    if (this.modoEdicao && this.registroSelecionadoId) {
      payload.id = this.registroSelecionadoId;
    }

    const requisicao$ = this.modoEdicao && this.registroSelecionadoId
      ? this.agendaService.update(this.registroSelecionadoId, payload)
      : this.agendaService.save(payload);

    requisicao$.subscribe({
      next: () => {
        Swal.fire({
          title: this.modoEdicao ? 'Evento atualizado!' : 'Evento criado!',
          icon: 'success',
          confirmButtonText: 'OK'
        });
        this.closeModal();
      },
      error: (err) => {
        Swal.fire({
          title: 'Erro!',
          text: `Não foi possível salvar o evento. Detalhes: ${err.message}`,
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    });
  }

  closeModal(): void {
    this.form.reset({ tipo: 'Processo', horario: '09:00', prioridade: 'NORMAL', prazoImportante: false });
    this.close.emit();
  }
}
