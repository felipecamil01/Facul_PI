import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';
import localePt from '@angular/common/locales/pt';
import { ClienteService } from '../../services/cliente.service';
import { AgendaService } from '../../services/agenda.service';
import { Agenda } from '../../models/agenda.model';
import { Router, ActivatedRoute } from '@angular/router';

registerLocaleData(localePt, 'pt-BR');

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './agenda-save.component.html',
  styleUrls: ['./agenda-save.component.scss']
})

export class AgendaSaveComponent implements OnInit {
  agenda: Agenda[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  selectedClienteId:number=0;
  clientes: any[] = [];

  statusOptions = ['PENDENTE', 'PAGO', 'ATRASADO', 'ESTORNADO'];
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  categoria = ['TRANSPORTE', 'ALIMENTAÇÃO', 'ESCRITÓRIO', 'CAPACITAÇÃO', 'PUBLICIDADE', 'LICENÇAS'];

  constructor(
    private route:ActivatedRoute,
    private router:Router,
    private agendaService: AgendaService,
    private fb: FormBuilder,
    private clienteService:ClienteService
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

     if(this.route.snapshot.paramMap.get('id')){
        const id = Number(this.route.snapshot.paramMap.get('id'));
        this.agendaService.findById(id).subscribe({
          next: agenda => {
            this.editarRegistro(agenda)
            console.log(agenda);
          },
          error: err =>{
            console.log(err)
          }
        }
        )
     }
     
  }

  carregarRegistros(): void {
    this.agendaService.findAll().subscribe({
      next: (data) => {
        this.agenda = data;
      },
      error: (error) => {
        console.error('Erro ao carregar registros:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) {


      if (this.modoEdicao && this.registroSelecionadoId) {
        const dadosParaSalvar = {...this.form.value};
        this.agendaService.update(this.registroSelecionadoId, dadosParaSalvar).subscribe({
          next: () => {
            Swal.fire({
              title: 'Atualizado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros();
            this.limparFormulario();
            this.router.navigate(['principal/agendas']);
          },
          error: (error) =>
            Swal.fire({
              title: 'Erro ao Atualizar',
              icon: 'error',
              confirmButtonText: 'OK'
            })
        });
      } else {
        this.clienteService.findById(this.form.value.clienteId).subscribe({
          next:(cliente)=>{
            const dadosParaSalvar = {
              ...this.form.value,
              honorario: this.removerFormatacaoMoeda(this.form.value.honorario),
        despesasAdicionais: this.removerFormatacaoMoeda(this.form.value.despesasAdicionais),
              cliente: cliente
            };
            this.agendaService.save(dadosParaSalvar).subscribe({
              next: () => {
                Swal.fire({
                  title: 'Cadastrado com sucesso',
                  icon: 'success',
                  confirmButtonText: 'OK'
                });
                this.carregarRegistros();
                this.limparFormulario();
                this.router.navigate(['principal/agendas']);
              },
              error: (error) =>
                Swal.fire({
                  title: 'Erro ao salvar',
                  icon: 'error',
                  confirmButtonText: 'OK'
                })
            });
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao buscar cliente',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        })

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
    }).then((result) => {
      if (result.isConfirmed) {
        this.agendaService.delete(id).subscribe({
          next: () => {
            Swal.fire({
              title: 'Deletado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros();
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao deletar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  limparFormulario(): void {
    this.form.reset();
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
  }

  corDoStatus(status: string): string {
    switch (status) {
      case 'PENDENTE':
        return 'badge bg-warning';
      case 'PAGO':
        return 'badge bg-success';
      case 'ATRASADO':
        return 'badge bg-danger';
      case 'ESTORNADO':
        return 'badge bg-dark';
      default:
        return '';
    }
  }

  formatCurrency(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value;

    value = value.replace(/\D/g, '');
    
    if (value === '') {
      value = '0';
    }

    const numericValue = (parseInt(value) / 100);

    const formattedValue = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(numericValue);

    input.value = formattedValue;

    const fieldName = input.getAttribute('formControlName');
    if (fieldName) {
      this.form.get(fieldName)?.setValue(formattedValue, { emitEvent: false });
    }
  }

  private removerFormatacaoMoeda(valor: string | number): number {
    if (valor == null) {
      return 0;
    }

    if (typeof valor === 'number') {
      return valor;
    }

    const valorLimpo = valor.replace(/[R$\s.]/g, '').replace(',', '.');
    const valorNumerico = parseFloat(valorLimpo);

    return isNaN(valorNumerico) ? 0 : valorNumerico;
}
carregaCliente() {
  this.clienteService.findAll().subscribe(
    (data) => this.clientes = data,
    (error) => console.error('Erro ao buscar clientes', error)
  );
}
}