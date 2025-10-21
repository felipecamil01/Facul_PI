import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AgendaService } from '../../../services/agenda.service';
import { RouterModule } from '@angular/router';
import { LoginService } from '../../../auth/login.service';
import { ProcessoService } from '../../../services/processo.service';

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

  @Input() eventoParaEditar?: any;
  @Input() selectedDate!: Date;
  @Output() close = new EventEmitter<void>();

  constructor(
    private agendaService: AgendaService,
    private fb: FormBuilder,
    private processoService: ProcessoService,
  ) {
    this.form = this.fb.group({
      titulo: ['', [Validators.required]],
      descricao: [''],
      tipo: ['', [Validators.required]],
      horario: ['', [Validators.required]],
      processoId: [null]
    });
  }

  ngOnInit(): void {
    this.carregarProcessos();

    if (this.eventoParaEditar) {
      this.carregarDadosParaEdicao(this.eventoParaEditar);
    } else if (this.selectedDate) {
      this.carregarDadosParaNovoEvento(this.selectedDate);
    }
  }

  private carregarProcessos(): void {
    this.processoService.findAll().subscribe({
      next: (lista) => this.processos = lista,
      error: () => console.error('Não foi possível carregar processos')
    });
  }

  private carregarDadosParaEdicao(evento: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = evento.id;
    const dataEvento = new Date(evento.data);

    this.form.patchValue({
      titulo: evento.titulo || evento.descricao || '',
      descricao: evento.descricao || '',
      tipo: evento.tipo || '',
      horario: dataEvento.toTimeString().slice(0, 5),
      processoId: evento.processo?.id || null,
    });
  }

  private carregarDadosParaNovoEvento(data: Date): void {
    this.modoEdicao = false;
    this.form.patchValue({ horario: '19:00' });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const [h, m] = this.form.value.horario.split(':');
      const dataFinal = new Date(this.selectedDate);
      dataFinal.setHours(parseInt(h, 10));
      dataFinal.setMinutes(parseInt(m, 10));

      const dadosParaSalvar = {
        // backend usa descricao/Tipo/data e aceita processo associado
        descricao: this.form.value.titulo || this.form.value.descricao,
        tipo: this.form.value.tipo,
        data: dataFinal.toISOString(),
        processo: this.form.value.processoId ? { id: this.form.value.processoId } : undefined,
      };

      if (this.modoEdicao && this.registroSelecionadoId) {
        (dadosParaSalvar as any).id = this.registroSelecionadoId;
      }

      this.agendaService.save(dadosParaSalvar as any).subscribe({
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
  }

  closeModal(): void {
    this.form.reset();
    this.close.emit();
  }
}
