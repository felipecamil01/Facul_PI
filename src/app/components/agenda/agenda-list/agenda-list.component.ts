import { Component, OnInit, LOCALE_ID } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { forkJoin } from 'rxjs';
import { AgendaFormComponent } from '../agenda-form/agenda-form.component';
import { AgendaService } from '../../../services/agenda.service';
import { Agenda } from '../../../models/agenda.model';
import localePt from '@angular/common/locales/pt';
import { PagamentoService } from '../../../services/pagamentoService';
import { Pagamento } from '../../../models/pagamento.model';

registerLocaleData(localePt);

interface CalendarDay {
  date: Date;
  inMonth: boolean;
}

type EventoStatus = 'PENDENTE' | 'PAGO' | 'ATRASADO';

type EventoOrigem = 'AGENDA' | 'PAGAMENTO';

interface AgendaCalendarEvent {
  id?: number;
  titulo: string;
  data: string;
  hora?: string;
  descricao?: string;
  tipo?: string;
  origem: EventoOrigem;
  status?: EventoStatus;
  valor?: number;
  numeroParcela?: number;
  totalParcelas?: number;
  cliente?: string;
}

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, AgendaFormComponent],
  providers: [{ provide: LOCALE_ID, useValue: 'pt-BR' }],
  templateUrl: './agenda-list.component.html',
  styleUrls: ['./agenda-list.component.scss']
})
export class AgendaListComponent implements OnInit {
  // === Variáveis de controle ===
  viewDate: Date = new Date();
  selectedDate: Date = new Date();

  weekdays: string[] = ['SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SAB','DOM'];
  weeks: CalendarDay[][] = [];

  showModal = false;  // modal do formulário
  modalOpen = false;  // modal de visualização de evento
  selectedEvent: AgendaCalendarEvent | null = null;

  events: AgendaCalendarEvent[] = [];

  constructor(
    private agendaService: AgendaService,
    private pagamentoService: PagamentoService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    this.generateCalendar();
  }

  // === Carrega eventos da API ===
  loadEvents(): void {
    forkJoin({
      agendas: this.agendaService.findAll(),
      pagamentos: this.pagamentoService.findAll()
    }).subscribe(({ agendas, pagamentos }) => {
      const eventosAgenda = agendas.map((evento) => this.mapearEventoAgenda(evento));
      const eventosPagamento = this.mapearEventosPagamento(pagamentos);
      this.events = [...eventosAgenda, ...eventosPagamento];
      this.generateCalendar();
    });
  }

  // === Gera a estrutura do calendário (6x7) ===
  generateCalendar(): void {
    const year = this.viewDate.getFullYear();
    const month = this.viewDate.getMonth();

    const firstDay = new Date(year, month, 1);
    const startDate = new Date(firstDay);
    startDate.setDate(firstDay.getDate() - ((firstDay.getDay() + 6) % 7)); // começa na segunda

    const days: CalendarDay[][] = [];
    let week: CalendarDay[] = [];

    for (let i = 0; i < 42; i++) {
      const current = new Date(startDate);
      const inMonth = current.getMonth() === month;
      week.push({ date: new Date(current), inMonth });

      startDate.setDate(startDate.getDate() + 1);
      if (week.length === 7) {
        days.push(week);
        week = [];
      }
    }

    this.weeks = days;
  }

  // === Retorna eventos de um dia ===
  eventsForDay(date: Date): AgendaCalendarEvent[] {
    return this.events.filter((event) => {
      const dataEvento = new Date(event.data);
      return dataEvento.toDateString() === date.toDateString();
    });
  }

  dayHasOverdue(date: Date): boolean {
    return this.eventsForDay(date).some((event) => event.status === 'ATRASADO');
  }

  // === Navegação entre meses ===
  prevMonth(): void {
    this.viewDate = new Date(this.viewDate.getFullYear(), this.viewDate.getMonth() - 1, 1);
    this.generateCalendar();
  }

  nextMonth(): void {
    this.viewDate = new Date(this.viewDate.getFullYear(), this.viewDate.getMonth() + 1, 1);
    this.generateCalendar();
  }

  // === Ações de modal ===
  openEvent(event: AgendaCalendarEvent, ev: MouseEvent): void {
    ev.stopPropagation();
    this.selectedEvent = event;
    this.modalOpen = true;
  }

  openNew(): void {
    this.selectedDate = new Date();
    this.selectedEvent = null;
    this.showModal = true;
  }

  onDateSelected(date: Date): void {
    this.selectedDate = date;
    this.selectedEvent = null;
    this.showModal = true;
  }

  closeModal(): void {
    this.modalOpen = false;
    this.showModal = false;
    this.selectedEvent = null;
    this.loadEvents();
  }

  private mapearEventoAgenda(evento: Agenda): AgendaCalendarEvent {
    const data = new Date(evento.data);
    return {
      id: evento.id,
      titulo: evento.titulo,
      data: evento.data,
      hora: evento.hora || this.formatarHorario(data),
      descricao: evento.descricao,
      tipo: evento.tipo,
      origem: 'AGENDA',
      status: this.definirStatusEventoGenerico(evento.data)
    };
  }

  private mapearEventosPagamento(pagamentos: Pagamento[]): AgendaCalendarEvent[] {
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    return pagamentos.flatMap((pagamento) => {
      if (!pagamento.parcelas || pagamento.parcelas.length === 0) {
        const dataReferencia = pagamento.dataPagamento || pagamento.dataCriacao || new Date().toISOString();
        return [
          {
            id: pagamento.id,
            titulo: `Pagamento ${pagamento.tipoPagamento === 'A_VISTA' ? 'à vista' : ''} - ${pagamento.cliente.nome}`,
            data: dataReferencia,
            hora: this.formatarHorario(dataReferencia),
            descricao: pagamento.observacao,
            tipo: 'Pagamento',
            origem: 'PAGAMENTO' as EventoOrigem,
            status: this.definirStatusPagamento(pagamento)
          }
        ];
      }

      return pagamento.parcelas.map((parcela) => {
        const dataVencimento = new Date(parcela.dataVencimento);
        dataVencimento.setHours(0, 0, 0, 0);

        const status: EventoStatus = parcela.statusPagamento === 'PAGO'
          ? 'PAGO'
          : dataVencimento < hoje
            ? 'ATRASADO'
            : 'PENDENTE';

        return {
          id: pagamento.id,
          titulo: `Parcela ${parcela.numeroParcela}/${pagamento.parcelas?.length ?? pagamento.numeroParcelas ?? 0} - ${pagamento.cliente.nome}`,
          data: parcela.dataVencimento,
          hora: '09:00',
          descricao: pagamento.observacao,
          tipo: 'Pagamento',
          origem: 'PAGAMENTO' as EventoOrigem,
          status,
          valor: parcela.valorParcela,
          numeroParcela: parcela.numeroParcela,
          totalParcelas: pagamento.parcelas?.length ?? pagamento.numeroParcelas ?? 0,
          cliente: pagamento.cliente.nome
        } satisfies AgendaCalendarEvent;
      });
    });
  }

  private definirStatusPagamento(pagamento: Pagamento): EventoStatus {
    if (pagamento.tipoPagamento === 'A_VISTA' && pagamento.dataPagamento) {
      return 'PAGO';
    }

    const possuiParcelas = pagamento.parcelas && pagamento.parcelas.length > 0;
    if (possuiParcelas && pagamento.parcelas!.some((parcela) => parcela.statusPagamento === 'ATRASADO')) {
      return 'ATRASADO';
    }

    if (possuiParcelas && pagamento.parcelas!.every((parcela) => parcela.statusPagamento === 'PAGO')) {
      return 'PAGO';
    }

    return 'PENDENTE';
  }

  private definirStatusEventoGenerico(dataIso: string): EventoStatus {
    const dataEvento = new Date(dataIso);
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    dataEvento.setHours(0, 0, 0, 0);

    if (dataEvento < hoje) {
      return 'ATRASADO';
    }

    return 'PENDENTE';
  }

  getBadgeClass(evento: AgendaCalendarEvent): string {
    switch (evento.status) {
      case 'PAGO':
        return 'pago';
      case 'ATRASADO':
        return 'atrasado';
      default:
        return 'pendente';
    }
  }

  getStatusTexto(evento: AgendaCalendarEvent): string {
    switch (evento.status) {
      case 'PAGO':
        return 'Pagamento confirmado';
      case 'ATRASADO':
        return 'Atrasado';
      default:
        return evento.origem === 'PAGAMENTO' ? 'No prazo' : 'Agendado';
    }
  }

  private formatarHorario(data: string | Date): string {
    const dataObj = typeof data === 'string' ? new Date(data) : data;
    if (Number.isNaN(dataObj.getTime())) {
      return '';
    }

    return dataObj.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  }
}
