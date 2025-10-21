import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HistoricoService } from '../../../services/historico.service';
import { HistoricoGeral } from '../../../models/historico.model';
import { ClienteService } from '../../../services/cliente.service';
import { ProcessoService } from '../../../services/processo.service';

@Component({
  selector: 'app-historico-list',
  standalone: true,
  templateUrl: './historico-list.component.html',
  styleUrls: ['./historico-list.component.scss'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule]
})
export class HistoricoComponent implements OnInit {
  historicoEspecifico: any[] = [];
  detalheSelecionado: any | null = null;

  tipoPesquisa: 'cliente' | 'pagamentos' | 'processo' | 'contato' = 'processo';
  idPesquisa: number | null = null;
  termoPesquisa: string = '';
  sugestoes: any[] = [];
  selecionadoId: number | null = null;

  private historicoService = inject(HistoricoService);
  private clienteService = inject(ClienteService);
  private processoService = inject(ProcessoService);

  ngOnInit(): void {
    // Carrega histórico geral ao entrar, caso backend esteja ativo
    this.historicoService.getHistoricoGeral().subscribe({
      next: (lista) => (this.historicoEspecifico = lista),
      error: () => (this.historicoEspecifico = [])
    });

    // Pré-carrega clientes para facilitar sugestões
    this.clienteService.findAll().subscribe({
      next: (lista: any[]) => {
        if (this.tipoPesquisa === 'cliente') this.sugestoes = lista;
      },
      error: () => {}
    });
  }

  pesquisarHistorico(): void {
    const alvoId = this.selecionadoId || this.idPesquisa;
    const termo = (this.termoPesquisa || '').trim();

    if (alvoId) {
      this.historicoService
        .getHistoricoPorEntidade(this.tipoPesquisa, alvoId)
        .subscribe({ next: (d) => (this.historicoEspecifico = d), error: () => (this.historicoEspecifico = []) });
      return;
    }

    // Sem ID: tenta resolver pelo termo (nome/numero)
    if (this.tipoPesquisa === 'cliente' && termo.length >= 2) {
      this.clienteService.findByNome(termo).subscribe({
        next: (lista: any[]) => {
          if (lista?.length) {
            this.historicoService
              .getHistoricoPorEntidade('cliente', lista[0].id)
              .subscribe({ next: (d) => (this.historicoEspecifico = d), error: () => (this.historicoEspecifico = []) });
          } else {
            this.historicoEspecifico = [];
          }
        },
        error: () => (this.historicoEspecifico = [])
      });
    } else if (this.tipoPesquisa === 'processo' && termo.length >= 2) {
      this.processoService.findByNumero(termo).subscribe({
        next: (lista: any[]) => {
          if (lista?.length) {
            this.historicoService
              .getHistoricoPorEntidade('processo', lista[0].id)
              .subscribe({ next: (d) => (this.historicoEspecifico = d), error: () => (this.historicoEspecifico = []) });
          } else {
            this.historicoEspecifico = [];
          }
        },
        error: () => (this.historicoEspecifico = [])
      });
    }
  }

  atualizarSugestoes(): void {
    const termo = (this.termoPesquisa || '').trim();
    this.sugestoes = [];
    this.selecionadoId = null;
    if (termo.length < 2) {
      if (this.tipoPesquisa !== 'cliente') this.sugestoes = [];
      return;
    }

    if (this.tipoPesquisa === 'cliente') {
      this.clienteService.findByNome(termo).subscribe({
        next: (lista: any[]) => (this.sugestoes = lista),
        error: () => (this.sugestoes = [])
      });
    } else if (this.tipoPesquisa === 'processo') {
      this.processoService.findByNumero(termo).subscribe({
        next: (lista: any[]) => (this.sugestoes = lista),
        error: () => (this.sugestoes = [])
      });
    }
  }

  escolherSugestao(value: string): void {
    if (this.tipoPesquisa === 'cliente') {
      const c = this.sugestoes.find((x: any) => x.nome === value);
      this.selecionadoId = c ? c.id : null;
    } else if (this.tipoPesquisa === 'processo') {
      const p = this.sugestoes.find((x: any) => x.numeroProcesso === value);
      this.selecionadoId = p ? p.id : null;
    }
  }

  abrirDetalhes(item: HistoricoGeral): void {
    this.detalheSelecionado = item;
  }

  resumoCampos(dados: any): { label: string; value: any }[] {
    if (!dados) return [];
    // Heurísticas simples por tipo de objeto
    if (dados.numeroProcesso) {
      return [
        { label: 'Processo', value: dados.numeroProcesso },
        { label: 'Cliente', value: dados.cliente?.nome ?? '-' },
        { label: 'Situação', value: dados.situacaoAtual ?? '-' },
        { label: 'Data Início', value: dados.dataInicio ?? '-' }
      ];
    }
    if (dados.nome && (dados.cpf || dados.email)) {
      return [
        { label: 'Cliente', value: dados.nome },
        { label: 'CPF', value: dados.cpf ?? '-' },
        { label: 'Email', value: dados.email ?? '-' }
      ];
    }
    if (dados.titulo || dados.descricao) {
      return [
        { label: 'Agenda', value: dados.titulo ?? dados.descricao },
        { label: 'Data', value: dados.data ?? '-' },
        { label: 'Tipo', value: dados.tipo ?? '-' }
      ];
    }
    // Fallback: mostra até 5 chaves principais
    const chaves = Object.keys(dados).slice(0, 5);
    return chaves.map(k => ({ label: k, value: (dados as any)[k] }));
  }
}
