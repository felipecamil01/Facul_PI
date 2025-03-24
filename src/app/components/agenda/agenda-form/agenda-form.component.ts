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

  @Input() selectedDate!: Date; // Agora opcional para evitar erros
  @Output() close = new EventEmitter<void>(); // Evento para fechar a modal

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
    if (this.selectedDate) {
      this.form.patchValue({ data: this.selectedDate.toISOString().split('T')[0] });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const dadosParaSalvar = { ...this.form.value };

      this.agendaService.save(dadosParaSalvar).subscribe({
        next: () => {
          Swal.fire({ title: 'Cadastrado com sucesso', icon: 'success', confirmButtonText: 'OK' });
          this.closeModal(); // Fecha a modal após salvar
        },
        error: () => {
          Swal.fire({ title: 'Erro ao salvar', icon: 'error', confirmButtonText: 'OK' });
        }
      });
    }
  }

  closeModal(): void {
    this.close.emit(); // Emite evento para fechar a modal
  }
}
