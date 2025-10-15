import { Component, OnInit, LOCALE_ID } from '@angular/core';
// IMPORTAÇÃO CORRIGIDA: Adicionamos CommonModule e DatePipe
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CalendarEvent, CalendarView, CalendarModule, DateAdapter } from 'angular-calendar'; // CalendarModule importado aqui
import { AgendaFormComponent } from '../agenda-form/agenda-form.component'; // AgendaFormComponent importado aqui
import { AgendaService } from '../../../services/agenda.service';
import { Agenda } from '../../../models/agenda.model';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import localePt from '@angular/common/locales/pt';

registerLocaleData(localePt);

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  // IMPORTAÇÕES CORRIGIDAS: Adicionamos todos os módulos necessários
  imports: [
    CommonModule,
    RouterModule,
    CalendarModule,       // Essencial para o <mwl-calendar-month-view>
    AgendaFormComponent,  // Essencial para o <app-agenda-form>
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: DateAdapter, useFactory: adapterFactory },
    DatePipe // Adicionamos o DatePipe aqui
  ],
  templateUrl: './agenda-list.component.html',
  styleUrls: ['./agenda-list.component.scss'],
})
export class AgendaListComponent implements OnInit {
  CalendarView = CalendarView;
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();
  selectedDate!: Date;
  selectedEvent!: any;
  showModal = false;

  events: CalendarEvent<{ agenda: Agenda }>[] = [];

  constructor(private agendaService: AgendaService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.agendaService.findAll().subscribe((agendaList) => {
      this.events = agendaList.map(agenda => {
        return {
          id: agenda.id,
          start: new Date(agenda.data),
          title: agenda.titulo, // Agora usando 'titulo' do nosso modelo corrigido
          color: { primary: '#e53935', secondary: '#ffcdd2' },
          meta: {
            agenda
          }
        };
      });
    });
  }

  onDayClick({ date }: { date: Date }): void {
    this.selectedDate = date;
    this.selectedEvent = undefined;
    this.showModal = true;
  }

  onEventClick({ event }: { event: CalendarEvent<{ agenda: Agenda }> }): void {
    if (event.meta?.agenda) {
      this.selectedDate = event.start;
      this.selectedEvent = {
        id: event.id,
        start: event.start,
        title: event.title,
        tipo: event.meta.agenda.tipo,
        descricao: event.meta.agenda.descricao,
      };
      this.showModal = true;
    }
  }
  
  closeModal(): void {
    this.showModal = false;
    this.loadEvents();
  }

  // ===============================================
  // MÉTODOS QUE ESTAVAM FALTANDO
  // ===============================================
  goToPreviousMonth(): void {
    const newDate = new Date(this.viewDate);
    newDate.setMonth(this.viewDate.getMonth() - 1);
    this.viewDate = newDate;
  }

  goToNextMonth(): void {
    const newDate = new Date(this.viewDate);
    newDate.setMonth(this.viewDate.getMonth() + 1);
    this.viewDate = newDate;
  }

  getToday(): Date {
    return new Date();
  }
}