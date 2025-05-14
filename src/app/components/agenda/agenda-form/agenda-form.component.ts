import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AgendaService } from '../../../services/agenda.service';
import { Agenda } from '../../../models/agenda.model';
import { StatusPagamento } from '../../../models/status-pagamento.enum';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-agenda-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './agenda-form.component.html',
  styleUrls: ['./agenda-form.component.scss']
})
export class AgendaFormComponent implements OnInit {
  loginService = inject(LoginService);
  agenda: Agenda[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  statusOptions = Object.values(StatusPagamento);

  @Input() eventoParaEditar?: any;
  @Input() selectedDate!: Date;
  @Output() close = new EventEmitter<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private agendaService: AgendaService,
    private fb: FormBuilder,
  ) {
    this.form = this.fb.group({
      data: ['', [Validators.required]],
      tipo: ['', [Validators.required]],
      descricao: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.resetForm(); // Reseta tudo antes de carregar os dados

    if (this.eventoParaEditar) {
      this.carregarDadosParaEdicao(this.eventoParaEditar);
    } else if (this.selectedDate) {
      this.carregarDadosParaNovoEvento(this.selectedDate);
    }
  }

  private formatDateForInput(date: Date): string {
    const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
    return localDate.toISOString().slice(0, 16);
  }
  

  private carregarDadosParaEdicao(evento: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = evento.id;

    const dataFormatada = this.formatDateForInput(new Date(evento.start));

    this.form.patchValue({
      data: dataFormatada,
      tipo: evento.tipo || '',
      descricao: evento.descricao || '',
    });
  }

  private carregarDadosParaNovoEvento(data: Date): void {
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;

    const dataFormatada = this.formatDateForInput(data);

    this.form.patchValue({ data: dataFormatada });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const dadosParaSalvar = { ...this.form.value };

      if (this.modoEdicao && this.registroSelecionadoId) {
        dadosParaSalvar.id = this.registroSelecionadoId;
      }

      this.agendaService.save(dadosParaSalvar).subscribe({
        next: () => {
          Swal.fire({
            title: this.modoEdicao ? 'Evento atualizado com sucesso' : 'Evento cadastrado com sucesso',
            icon: 'success',
            confirmButtonText: 'OK'
          });
          this.closeModal();
        },
        error: () => {
          Swal.fire({
            title: this.modoEdicao ? 'Erro ao atualizar evento' : 'Erro ao salvar evento',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    }
  }

  closeModal(): void {
    this.resetForm(); // Resetar formulário ao fechar modal
    this.close.emit();
  }

  private resetForm(): void {
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
    this.form.reset({
      data: '',
      tipo: '',
      descricao: '',
    });
  }
}
