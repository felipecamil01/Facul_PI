import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { ProcessoService } from '../../../services/processo.service';
import { ClienteService } from '../../../services/cliente.service';
import { LoginService } from '../../../auth/login.service';
import { DocumentoService } from '../../../services/documento.service';
import Swal from 'sweetalert2';
import { ClienteDTO } from '../../../models/ClienteDTO';

@Component({
  selector: 'app-processo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterModule],
  templateUrl: './processo-form.component.html',
  styleUrls: ['./processo-form.component.scss']
})
export class ProcessoFormComponent implements OnInit {
  loginService = inject(LoginService);
  documentoService = inject(DocumentoService);
  processoForm: FormGroup;
  clientes: ClienteDTO[] = [];
  statusDocumento: string[] = [];
  modoEdicao = false;
  outroSelectStates: boolean[] = [];
  registroSelecionadoId?: number;

  private fb = inject(FormBuilder);
  private processoService = inject(ProcessoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private clienteService = inject(ClienteService);

  constructor() {
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
      prazosImportantes: this.fb.array([]),
      documentos: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.carregaCliente();
    this.carregarStatusDocumento();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.processoService.findById(id).subscribe({
        next: processo => this.editarRegistro(processo),
        error: err => console.log(err)
      });
    }
  }

  get prazosImportantes(): FormArray {
    return this.processoForm.get('prazosImportantes') as FormArray;
  }

  get documentos(): FormArray {
    return this.processoForm.get('documentos') as FormArray;
  }

  carregaCliente(): void {
    this.clienteService.findAll().subscribe(
      data => this.clientes = data,
      error => console.error('Erro ao buscar clientes', error)
    );
  }

  carregarStatusDocumento(): void {
    this.documentoService.findStatusDocumento().subscribe((data: string[]) => {
      this.statusDocumento = data;
    });
  }

  editarRegistro(processo: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = processo.id;
    this.processoForm.patchValue({
      tipoCliente: processo.tipoCliente,
      areaAtuacao: processo.areaAtuacao,
      numeroProcesso: processo.numeroProcesso,
      comarca: processo.comarca,
      dataInicio: processo.dataInicio,
      descricao: processo.descricao,
      andamento: processo.andamento,
      situacaoAtual: processo.situacaoAtual,
      cliente: processo.cliente.id
    });

    this.prazosImportantes.clear();
    processo.prazosImportantes?.forEach((prazo: any) => this.adicionarPrazo(prazo));

    this.documentos.clear();
    processo.documentos?.forEach((documento: any) => this.adicionarDocumento(documento));
  }

  adicionarPrazo(prazo?: any): void {
    const grupo = this.fb.group({
      descricao: [prazo?.descricao || '', Validators.required],
      data: [prazo?.data ? this.toDatetimeLocal(prazo.data) : '', Validators.required]
    });
    this.prazosImportantes.push(grupo);
  }

  removerPrazo(index: number): void {
    this.prazosImportantes.removeAt(index);
  }

  private toDatetimeLocal(value?: string | Date | null): string {
    if (!value) {
      return '';
    }
    const date = value instanceof Date ? value : new Date(value);
    if (Number.isNaN(date.getTime())) {
      return '';
    }
    date.setSeconds(0, 0);
    const iso = date.toISOString();
    return iso.substring(0, 16);
  }

  private criarDocumentoFormGroup(documento?: any): FormGroup {
    return this.fb.group({
      titulo: [documento?.titulo || '', Validators.required],
      statusDocumento: [documento?.statusDocumento || '', Validators.required],
      outroStatusDocumento: [documento?.outroStatusDocumento || ''],
      dataRecebimento: [documento?.dataRecebimento || '', Validators.required],
      observacao: [documento?.observacao || ''],
      arquivo: [null]
    });
  }

  adicionarDocumento(documento?: any): void {
    const documentoForm = this.criarDocumentoFormGroup(documento);
    this.documentos.push(documentoForm);
    this.outroSelectStates.push(documento?.statusDocumento === 'OUTROS');
  }

  removerDocumento(index: number): void {
    this.documentos.removeAt(index);
    this.outroSelectStates.splice(index, 1);
  }

  onSubmit(): void {
    if (this.processoForm.invalid) {
      Swal.fire('Atenção!', 'Verifique os campos obrigatórios antes de salvar.', 'warning');
      return;
    }

    this.clienteService.findById(this.processoForm.value.cliente).subscribe({
      next: (cliente) => {
        const dadosProcesso = { ...this.processoForm.value, cliente: cliente };
        dadosProcesso.prazosImportantes = this.prazosImportantes.controls
          .map(control => control.value)
          .filter((prazo: any) => prazo && prazo.data)
          .map((prazo: any) => ({
            descricao: prazo.descricao,
            data: new Date(prazo.data).toISOString()
          }));

        const operacao = dadosProcesso.id ?
          this.processoService.update(dadosProcesso.id, dadosProcesso) :
          this.processoService.save(dadosProcesso);

        operacao.subscribe({
          next: () => {
            Swal.fire({
              title: dadosProcesso.id ? 'Processo atualizado!' : 'Processo criado!',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            const rota = this.loginService.hasPermission('ADMIN') ? 'admin/processo' : 'user/processo';
            this.router.navigate([rota]);
          },
          error: (erro) => {
            console.error('Erro ao salvar processo', erro);
            Swal.fire('Erro', 'Não foi possível salvar o processo', 'error');
          }
        });
      },
      error: () => {
        Swal.fire('Erro', 'Erro ao buscar cliente', 'error');
      }
    });
  }

  onStatusDocumentoChange(event: Event, index: number) {
    const selectElement = event.target as HTMLSelectElement;
    this.outroSelectStates[index] = selectElement.value === 'OUTROS';
    const outroStatusControl = this.documentos.at(index).get('outroStatusDocumento');

    if (!this.outroSelectStates[index]) {
      outroStatusControl?.setValue('');
    }
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}


