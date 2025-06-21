import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HistoricoService } from '../../../services/historico.service';
import { HistoricoGeral } from '../../../models/historico.model'; // <-- Importe a interface correta

@Component({
  selector: 'app-historico-list',
  standalone: true,
  templateUrl: './historico-list.component.html',
  styleUrls: ['./historico-list.component.scss'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule]
})
export class HistoricoComponent implements OnInit {
  // Array para o resultado da busca específica.
  // Pode ser do tipo Historico[] ou HistoricoGeral[], dependendo do que você quer mostrar.
  historicoEspecifico: any[] = []; // Usaremos 'any' para flexibilidade aqui
  detalheSelecionado: any | null = null;
  
  // Propriedades para o formulário de pesquisa
  tipoPesquisa: 'cliente' | 'despesa' | 'processo' | 'contato' = 'processo';
  idPesquisa: number | null = null;

  private historicoService = inject(HistoricoService);

  ngOnInit(): void {
    // Agora é opcional carregar o histórico geral aqui. 
    // Talvez você queira que a lista comece vazia e só popule após uma busca.
    // this.historicoService.getHistoricoGeral().subscribe(...)
  }

  // NOVO MÉTODO para lidar com a pesquisa
  pesquisarHistorico(): void {
    if (!this.idPesquisa) {
      return; // Não faz nada se o ID não for preenchido
    }
    
    this.historicoService.getHistoricoPorEntidade(this.tipoPesquisa, this.idPesquisa)
      .subscribe({
        next: data => {
          // Você pode exibir o resultado em uma nova tabela ou na mesma.
          // Por exemplo, limpamos a lista geral e mostramos a específica
          this.historicoEspecifico = data; 
          console.log('Resultado da pesquisa:', data);
        },
        error: err => {
          console.error('Erro na pesquisa de histórico:', err);
          this.historicoEspecifico = []; // Limpa em caso de erro
        }
      });
  }

  abrirDetalhes(item: HistoricoGeral): void {
    this.detalheSelecionado = item;
    const modalElement = document.getElementById('modalDetalhes');
    if (modalElement) {
      // @ts-ignore
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }
}