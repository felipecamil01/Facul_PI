import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteDTO } from '../../../models/ClienteDTO';
import { Documento } from '../../../models/documento.model';
import { Processo } from '../../../models/processo.model';
import { ClienteOverview } from '../../../models/cliente-overview.model';
import { ClienteService } from '../../../services/cliente.service';
import { DocumentoService } from '../../../services/documento.service';
import { ProcessoService } from '../../../services/processo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-documento-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './documento-list.component.html',
  styleUrls: ['./documento-list.component.scss']
})
export class DocumentoListComponent implements OnInit {
  clientes: ClienteDTO[] = [];
  processos: Processo[] = [];
  documentos: Documento[] = [];
  documentosFiltrados: Documento[] = [];
  statusOptions: string[] = [];

  filtroClienteId: number | null = null;
  filtroTexto = '';
  carregandoDocumentos = false;
  salvando = false;

  uploadForm: FormGroup;
  arquivoSelecionado?: File;
  overview?: ClienteOverview;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private documentoService: DocumentoService,
    private processoService: ProcessoService
  ) {
    this.uploadForm = this.fb.group({
      clienteId: [null, Validators.required],
      processoId: [null, Validators.required],
      titulo: ['', Validators.required],
      statusDocumento: ['', Validators.required],
      dataRecebimento: [''],
      observacao: ['']
    });
  }

  ngOnInit(): void {
    this.carregarClientes();
    this.carregarStatus();
    this.uploadForm.get('clienteId')?.valueChanges.subscribe((clienteId) => {
      if (clienteId) {
        this.carregarProcessos(clienteId);
      } else {
        this.processos = [];
        this.uploadForm.get('processoId')?.reset();
      }
    });
  }

  carregarClientes(): void {
    this.clienteService.findAll().subscribe({
      next: (lista) => (this.clientes = lista),
      error: () => console.error('Não foi possível carregar clientes')
    });
  }

  carregarStatus(): void {
    this.documentoService.findStatusDocumento().subscribe({
      next: (status) => {
        this.statusOptions = status && status.length ? status : ['RECEBIDO', 'PENDENTE', 'ENTREGUE'];
        this.uploadForm.patchValue({ statusDocumento: this.statusOptions[0] });
      },
      error: () => {
        this.statusOptions = ['RECEBIDO', 'PENDENTE', 'ENTREGUE'];
        this.uploadForm.patchValue({ statusDocumento: this.statusOptions[0] });
      }
    });
  }

  carregarProcessos(clienteId: number): void {
    this.processoService.findByCliente(clienteId).subscribe({
      next: (lista) => (this.processos = lista),
      error: () => console.error('Não foi possível carregar processos do cliente')
    });
  }

  selecionarClienteFiltro(id: number | null): void {
    this.filtroClienteId = id ? Number(id) : null;
    if (this.filtroClienteId) {
      this.carregarProcessos(this.filtroClienteId);
      this.carregarDocumentosDoCliente(this.filtroClienteId);
      this.carregarOverview(this.filtroClienteId);
    } else {
      this.documentos = [];
      this.documentosFiltrados = [];
      this.processos = [];
      this.overview = undefined;
    }
  }

  carregarDocumentosDoCliente(clienteId: number): void {
    this.carregandoDocumentos = true;
    this.documentoService.findByCliente(clienteId).subscribe({
      next: (docs) => {
        this.documentos = docs || [];
        this.aplicarFiltroTexto();
        this.carregandoDocumentos = false;
      },
      error: () => {
        this.carregandoDocumentos = false;
        Swal.fire('Erro', 'Não foi possível carregar os documentos do cliente.', 'error');
      }
    });
  }

  carregarOverview(clienteId: number): void {
    this.clienteService.getOverview(clienteId).subscribe({
      next: (overview) => (this.overview = overview),
      error: () => console.error('Não foi possível carregar a visão geral do cliente')
    });
  }

  aplicarFiltroTexto(): void {
    if (!this.filtroTexto) {
      this.documentosFiltrados = [...this.documentos];
      return;
    }
    const termo = this.filtroTexto.toLowerCase();
    this.documentosFiltrados = this.documentos.filter((doc) =>
      (doc.titulo || '').toLowerCase().includes(termo) ||
      (doc.processo?.numeroProcesso || '').toLowerCase().includes(termo)
    );
  }

  selecionarArquivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.arquivoSelecionado = input.files && input.files.length ? input.files[0] : undefined;
  }

  async enviarDocumento(): Promise<void> {
    if (this.uploadForm.invalid || !this.arquivoSelecionado) {
      Swal.fire('Atenção', 'Preencha todos os campos e selecione um arquivo PDF.', 'warning');
      return;
    }

    this.salvando = true;
    const formValue = this.uploadForm.value;
    try {
      const base64 = await this.converterArquivoParaBase64(this.arquivoSelecionado);
      const payload: Documento = {
        titulo: formValue.titulo,
        statusDocumento: formValue.statusDocumento,
        observacao: formValue.observacao,
        dataRecebimento: formValue.dataRecebimento ? this.formatarData(formValue.dataRecebimento) : undefined,
        nomeArquivo: this.arquivoSelecionado.name,
        arquivo: base64,
        processo: { id: Number(formValue.processoId) } as Processo
      };

      this.documentoService.save(payload).subscribe({
        next: () => {
          Swal.fire('Sucesso', 'Documento salvo com sucesso.', 'success');
          const clienteAtual = this.uploadForm.value.clienteId;
          this.uploadForm.reset({
            clienteId: clienteAtual,
            processoId: null,
            titulo: '',
            statusDocumento: this.statusOptions[0],
            dataRecebimento: '',
            observacao: ''
          });
          this.arquivoSelecionado = undefined;
          if (clienteAtual) {
            this.carregarProcessos(clienteAtual);
          }
          if (this.filtroClienteId) {
            this.carregarDocumentosDoCliente(this.filtroClienteId);
            this.carregarOverview(this.filtroClienteId);
          }
          this.salvando = false;
        },
        error: (err) => {
          this.salvando = false;
          Swal.fire('Erro', err?.error?.message || 'Não foi possível salvar o documento.', 'error');
        }
      });
    } catch (error) {
      this.salvando = false;
      Swal.fire('Erro', 'Falha ao processar o arquivo selecionado.', 'error');
    }
  }

  download(doc: Documento): void {
    if (!doc.id) return;
    this.documentoService.download(doc.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = doc.nomeArquivo || `${doc.titulo}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => Swal.fire('Erro', 'Não foi possível baixar o documento.', 'error')
    });
  }

  private converterArquivoParaBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        const base64 = result.split(',')[1];
        resolve(base64);
      };
      reader.onerror = (err) => reject(err);
      reader.readAsDataURL(file);
    });
  }

  private formatarData(value: string): string {
    const data = new Date(value);
    const ano = data.getFullYear();
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const dia = String(data.getDate()).padStart(2, '0');
    return `${ano}-${mes}-${dia}`;
  }
}

