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

interface AgendaEvent {
  id?: number;
  titulo?: string;
  descricao?: string;
  tipo?: string;
  data: string;
  hora?: string;
  processo?: any;
  cliente?: any;
  prazoImportante?: boolean;
  prioridade?: string;
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
  private readonly MAX_VISIBLE_EVENTS = 3;

  viewDate: Date = new Date();
  selectedDate: Date = new Date();
  weekdays: string[] = ['SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SÁB', 'DOM'];
  weeks: CalendarDay[][] = [];
  weeklyAgenda: { date: Date; eventos: AgendaEvent[] }[] = [];

  showModal = false;
  modalOpen = false;
  selectedEvent: AgendaEvent | null = null;
  selectedDayEvents: AgendaEvent[] = [];

  events: AgendaEvent[] = [];
  expandedDays = new Set<string>();

  constructor(private agendaService: AgendaService) {}

  ngOnInit(): void {
    this.loadEvents();
    this.generateCalendar();
    this.selectedDayEvents = this.eventsForDay(this.selectedDate);
    this.refreshWeeklyOverview();
  }

  loadEvents(): void {
    this.agendaService.findAll().subscribe((agendaList: any[]) => {
      this.events = agendaList.map((a: any) => {
        const dataStr = a.data;
        const dt = dataStr ? new Date(dataStr) : undefined;
        const hora = dt
          ? dt.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
          : '';
        return {
          id: a.id ?? 0,
          titulo: a.titulo,
          descricao: a.descricao,
          tipo: a.tipo,
          data: dataStr,
          hora,
          processo: a.processo,
          cliente: a.cliente ?? a.processo?.cliente,
          prazoImportante: !!a.prazoImportante || (a.tipo && a.tipo === 'PRAZO_IMPORTANTE'),
          prioridade: a.prioridade
        } as AgendaEvent;
      });
      this.generateCalendar();
      this.selectedDayEvents = this.eventsForDay(this.selectedDate);
      this.refreshWeeklyOverview();
    });
  }

  generateCalendar(): void {
    const year = this.viewDate.getFullYear();
    const month = this.viewDate.getMonth();

    const firstDay = new Date(year, month, 1);
    const startDate = new Date(firstDay);
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

  eventsForDay(date: Date): AgendaEvent[] {
    const key = this.dayKey(date);
    return this.events.filter((e) => this.dayKey(new Date(e.data)) === key);
  }

  previewEvents(date: Date): AgendaEvent[] {
    const dayEvents = this.eventsForDay(date);
    if (this.expandedDays.has(this.dayKey(date))) {
      return dayEvents;
    }
    return dayEvents.slice(0, this.MAX_VISIBLE_EVENTS);
  }

  hasMoreEvents(date: Date): boolean {
    return this.eventsForDay(date).length > this.MAX_VISIBLE_EVENTS;
  }

  toggleDayExpansion(date: Date, event: MouseEvent): void {
    event.stopPropagation();
    const key = this.dayKey(date);
    if (this.expandedDays.has(key)) {
      this.expandedDays.delete(key);
    } else {
      this.expandedDays.add(key);
    }
  }

  prevMonth(): void {
    this.viewDate = new Date(this.viewDate.getFullYear(), this.viewDate.getMonth() - 1, 1);
    this.generateCalendar();
    this.selectedDayEvents = this.eventsForDay(this.selectedDate);
    this.refreshWeeklyOverview();
  }

  nextMonth(): void {
    this.viewDate = new Date(this.viewDate.getFullYear(), this.viewDate.getMonth() + 1, 1);
    this.generateCalendar();
    this.selectedDayEvents = this.eventsForDay(this.selectedDate);
    this.refreshWeeklyOverview();
  }

  openEvent(event: AgendaEvent, mouseEvent: MouseEvent): void {
    mouseEvent.stopPropagation();
    this.selectedEvent = event;
    this.modalOpen = true;
  }

  onEditEventClick(eventItem: AgendaEvent, mouseEvent: MouseEvent): void {
    mouseEvent.stopPropagation();
    this.selectedEvent = eventItem;
    this.modalOpen = false;
    this.showModal = true;
  }

  onDeleteEventClick(eventItem: AgendaEvent, mouseEvent: MouseEvent): void {
    mouseEvent.stopPropagation();
    if (!eventItem?.id) return;
    const ok = confirm('Deseja deletar este evento?');
    if (!ok) return;
    this.agendaService.delete(eventItem.id).subscribe({
      next: () => {
        this.loadEvents();
        this.selectedDayEvents = this.eventsForDay(this.selectedDate);
      },
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
      next: () => this.closeModal(),
      error: (e) => console.error('Erro ao deletar evento', e)
    });
  }

  openNew(): void {
    this.selectedEvent = undefined as any;
    if (!this.selectedDate) {
      this.selectedDate = new Date();
    }
    this.showModal = true;
  }

  onDateSelected(date: Date): void {
    this.selectedDate = date;
    const key = this.dayKey(date);
    this.expandedDays.add(key);
    this.selectedDayEvents = this.eventsForDay(date);
    this.refreshWeeklyOverview();
  }

  closeModal(): void {
    this.modalOpen = false;
    this.showModal = false;
    this.loadEvents();
    this.selectedDayEvents = this.eventsForDay(this.selectedDate);
  }

  isPrazoImportante(evento: AgendaEvent): boolean {
    return !!evento.prazoImportante || evento.tipo === 'PRAZO_IMPORTANTE';
  }

  isSelectedDay(date: Date): boolean {
    return this.dayKey(date) === this.dayKey(this.selectedDate);
  }

  dayKey(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  private refreshWeeklyOverview(): void {
    const base = new Date(this.selectedDate);
    const dayOfWeek = base.getDay();
    const mondayOffset = (dayOfWeek + 6) % 7;
    const weekStart = new Date(base);
    weekStart.setDate(base.getDate() - mondayOffset);

    this.weeklyAgenda = Array.from({ length: 7 }).map((_, idx) => {
      const date = new Date(weekStart);
      date.setDate(weekStart.getDate() + idx);
      return {
        date,
        eventos: this.eventsForDay(date).sort((a, b) => (a.hora || '').localeCompare(b.hora || ''))
      };
    });
  }
}
