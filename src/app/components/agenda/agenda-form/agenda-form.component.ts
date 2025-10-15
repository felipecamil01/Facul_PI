import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AgendaService } from '../../../services/agenda.service';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-agenda-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './agenda-form.component.html',
  styleUrls: ['./agenda-form.component.scss'] // O estilo será aplicado aqui
})
export class AgendaFormComponent implements OnInit {
  loginService = inject(LoginService);
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;

  @Input() eventoParaEditar?: any;
  @Input() selectedDate!: Date; // Recebe a data completa do dia clicado
  @Output() close = new EventEmitter<void>();

  constructor(
    private agendaService: AgendaService,
    private fb: FormBuilder,
  ) {
    // MODIFICAÇÃO: Ajustamos os campos do formulário
    this.form = this.fb.group({
      titulo: ['', [Validators.required]],
      descricao: [''],
      tipo: ['', [Validators.required]],
      horario: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (this.eventoParaEditar) {
      this.carregarDadosParaEdicao(this.eventoParaEditar);
    } else if (this.selectedDate) {
      this.carregarDadosParaNovoEvento(this.selectedDate);
    }
  }

  private carregarDadosParaEdicao(evento: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = evento.id;
    const dataEvento = new Date(evento.start);

    this.form.patchValue({
      titulo: evento.title,
      descricao: evento.descricao || '',
      tipo: evento.tipo || '',
      // Extrai o horário no formato "HH:mm"
      horario: dataEvento.toTimeString().slice(0, 5),
    });
  }

  private carregarDadosParaNovoEvento(data: Date): void {
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
    this.form.patchValue({
      // Define um horário padrão ao criar um novo evento
      horario: '19:00'
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      // Combina a data do calendário com o horário do formulário
      const [horas, minutos] = this.form.value.horario.split(':');
      const dataFinal = new Date(this.selectedDate);
      dataFinal.setHours(parseInt(horas, 10));
      dataFinal.setMinutes(parseInt(minutos, 10));
      dataFinal.setSeconds(0);

      const dadosParaSalvar = {
        titulo: this.form.value.titulo,
        descricao: this.form.value.descricao,
        tipo: this.form.value.tipo,
        data: dataFinal.toISOString(), // Envia a data completa para o backend
      };

      if (this.modoEdicao && this.registroSelecionadoId) {
        (dadosParaSalvar as any).id = this.registroSelecionadoId;
      }

      this.agendaService.save(dadosParaSalvar).subscribe({
        
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