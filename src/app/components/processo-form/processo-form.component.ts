// processo-form.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  FormBuilder, 
  FormGroup, 
  FormArray, 
  ReactiveFormsModule, 
  Validators 
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProcessoService } from '../../services/processo.service';
import { Cliente } from '../../models/cliente.model';
import Swal from 'sweetalert2';
import { ClienteService } from '../../services/cliente.service';



@Component({
  selector: 'app-processo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink],
  templateUrl: './processo-form.component.html',
  styleUrl: './processo-form.component.scss'
})
export class ProcessoFormComponent implements OnInit {
  processoForm: FormGroup;
  clientes: Cliente[] = [];

  private fb = inject(FormBuilder);
  private processoService = inject(ProcessoService);
  private router = inject(Router);
  private clienteService = inject(ClienteService);

  constructor(){
    this.processoForm = this.fb.group({
      id: [null],
      tipoCliente: ['', Validators.required],
      areaAtuacao: ['', Validators.required],
      numeroProcesso: ['', [Validators.required, Validators.minLength(3)]],
      comarca: ['', Validators.required],
      dataInicio: ['', Validators.required],
      descricao: ['', Validators.required],
      andamento: [''],
      situacaoAtual: ['', Validators.required],
      cliente: [null, Validators.required],
      prazosImportantes: this.fb.array([])
    });
  }
  ngOnInit(): void {
    // Inicializar o formulário
  /*
    this.processoForm = this.fb.group({
      id: [null],
      tipoCliente: ['', Validators.required],
      areaAtuacao: ['', Validators.required],
      numeroProcesso: ['', [Validators.required, Validators.minLength(3)]],
      comarca: ['', Validators.required],
      dataInicio: ['', Validators.required],
      descricao: ['', Validators.required],
      andamento: [''],
      situacaoAtual: ['', Validators.required],
      cliente: [null, Validators.required],
      prazosImportantes: this.fb.array([])
    });
*/
    // Carregar clientes
    this.carregaCliente();

    // Se for edição, carregar dados do processo
    this.loadProcessoParaEdicao();
  }

  // Getter para prazos importantes como FormArray
  get prazosImportantes() {
    return this.processoForm.get('prazosImportantes') as FormArray;
  }

  // Carregar lista de clientes
  carregaCliente(): void {
    this.clienteService.findAll().subscribe(
      data => this.clientes = data,
      error => console.error('Erro ao buscar clientes', error)
    );
  }

  // Carregar processo para edição (se aplicável)
  private loadProcessoParaEdicao(): void {
    const processoId = this.router.getCurrentNavigation()?.extras?.state?.['processoId'];
    if (processoId) {
      this.processoService.findById(processoId).subscribe({
        next: (processo) => {
          this.processoForm.patchValue(processo);
          // Se houver prazos, adicionar ao FormArray
          processo.prazosImportantes?.forEach(prazo => {
            this.adicionarPrazo(prazo);
          });
        },
        error: (erro) => {
          console.error('Erro ao carregar processo', erro);
          Swal.fire('Erro', 'Não foi possível carregar o processo', 'error');
        }
      });
    }
  }

  // Adicionar prazo
  adicionarPrazo(prazo: Date | null): void {
    const prazoControl = this.fb.control(prazo || '');
    this.prazosImportantes.push(prazoControl);
  }

  // Remover prazo
  removerPrazo(index: number): void {
    this.prazosImportantes.removeAt(index);
  }

  // Submeter formulário
  onSubmit(): void {
    if (this.processoForm.valid) {
      const dadosProcesso = this.processoForm.value;
      
      // Decidir entre criar ou atualizar
      const operacao = dadosProcesso.id ? 
        this.processoService.update(dadosProcesso.id,dadosProcesso) : 
        this.processoService.save(dadosProcesso);

      operacao.subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: dadosProcesso.id ? 'Processo atualizado!' : 'Processo criado!',
            showConfirmButton: false,
            timer: 1500
          });
          this.router.navigate(['/processos']);
        },
        error: (erro) => {
          console.error('Erro ao salvar processo', erro);
          Swal.fire('Erro', 'Não foi possível salvar o processo', 'error');
        }
      });
    } else {
      // Marcar todos os campos como tocados para mostrar validações
      this.markFormGroupTouched(this.processoForm);
    }
  }

  // Método para marcar todos os campos como tocados
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // Método para verificar se um campo está inválido
  campoInvalido(nomeCampo: string): boolean {
    const campo = this.processoForm.get(nomeCampo);
    return !!(campo && campo.invalid && (campo.dirty || campo.touched));
  }
}