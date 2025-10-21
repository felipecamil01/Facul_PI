import { Component, OnInit, LOCALE_ID } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AgendaFormComponent } from '../agenda-form/agenda-form.component';
import { AgendaService } from '../../../services/agenda.service';
import localePt from '@angular/common/locales/pt';

registerLocaleData(localePt);

interface CalendarDay {
  date: Date;
  inMonth: boolean;
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

  weekdays: string[] = ['SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SAB', 'DOM'];
  weeks: CalendarDay[][] = [];

  showModal = false;  // modal do formulário
  modalOpen = false;  // modal de visualização de evento
  selectedEvent: any = null;

  events: { id?: number; titulo?: string; descricao?: string; tipo?: string; data: string; hora?: string; processo?: any }[] = [];

  constructor(private agendaService: AgendaService) { }

  ngOnInit(): void {
    this.loadEvents();
    this.generateCalendar();
  }

  // === Carrega eventos da API ===
  loadEvents(): void {
    this.agendaService.findAll().subscribe((agendaList: any[]) => {
      this.events = agendaList.map((a: any) => {
        const dataStr = a.data;
        const dt = dataStr ? new Date(dataStr) : undefined;
        const hora = dt ? dt.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }) : '';
        return {
          id: a.id ?? 0,
          titulo: a.titulo,
          descricao: a.descricao,
          tipo: a.tipo ?? a.Tipo,
          data: dataStr,
          hora,
          processo: a.processo
        };
      });
      this.generateCalendar();
    });
  }

  // === Gera a estrutura do calendário (6x7) ===
  generateCalendar(): void {
    const year = this.viewDate.getFullYear();
    const month = this.viewDate.getMonth();

    const firstDay = new Date(year, month, 1);
    const startDate = new Date(firstDay);
    // começa na segunda
    startDate.setDate(firstDay.getDate() - ((firstDay.getDay() + 6) % 7));

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
  eventsForDay(date: Date) {
    return this.events.filter(e => {
      const d = new Date(e.data);
      return d.toDateString() === date.toDateString();
    });
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
  openEvent(event: any, ev: MouseEvent): void {
    ev.stopPropagation();
    this.selectedEvent = event;
    this.modalOpen = true;
  }

  onEditEventClick(eventItem: any, ev: MouseEvent): void {
    ev.stopPropagation();
    this.selectedEvent = eventItem;
    this.modalOpen = false;
    this.showModal = true; // abre o formulário em modo edição
  }

  onDeleteEventClick(eventItem: any, ev: MouseEvent): void {
    ev.stopPropagation();
    if (!eventItem?.id) return;
    const ok = confirm('Deseja deletar este evento?');
    if (!ok) return;
    this.agendaService.delete(eventItem.id).subscribe({
      next: () => this.loadEvents(),
      error: (e) => console.error('Erro ao deletar evento', e)
    });
  }

  editSelected(): void {
    if (!this.selectedEvent) return;
    this.modalOpen = false;
    this.showModal = true;
  }

  deleteSelected(): void {
    if (!this.selectedEvent?.id) return;
    const ok = confirm('Deseja deletar este evento?');
    if (!ok) return;
    this.agendaService.delete(this.selectedEvent.id).subscribe({
      next: () => {
        this.closeModal();
      },
      error: (e) => console.error('Erro ao deletar evento', e)
    });
  }

  openNew(): void {
    this.selectedDate = new Date();
    this.selectedEvent = undefined;
    this.showModal = true;
  }

  onDateSelected(date: Date): void {
    this.selectedDate = date;
    this.selectedEvent = undefined;
    this.showModal = true;
  }

  closeModal(): void {
    this.modalOpen = false;
    this.showModal = false;
    this.loadEvents();
  }
}

