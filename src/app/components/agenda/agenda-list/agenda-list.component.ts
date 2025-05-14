import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CalendarA11y, CalendarDateFormatter, CalendarEvent, CalendarEventTitleFormatter, CalendarUtils, CalendarView } from 'angular-calendar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { AgendaFormComponent } from '../agenda-form/agenda-form.component';
import { AgendaService } from '../../../services/agenda.service';
import { Agenda } from '../../../models/agenda.model';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { LOCALE_ID } from '@angular/core';
import ptBR from 'date-fns/locale/pt-BR';
import { CustomDateFormatter } from '../../../utils/custom-date-formatter'; // ou o caminho correto


registerLocaleData(localePt); // <- registra o locale do Angular

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    CalendarModule,
    AgendaFormComponent
  ],
  providers: [
    {
      provide: DateAdapter,
      useFactory: adapterFactory,
    },
    {
      provide: LOCALE_ID,
      useValue: 'pt-BR',
    },
    {
      provide: CalendarDateFormatter,
      useClass: CustomDateFormatter,
    },
    CalendarUtils,
    CalendarA11y,
    CalendarDateFormatter,
    CalendarEventTitleFormatter
  ],
  templateUrl: './agenda-list.component.html',
  styleUrls: ['./agenda-list.component.scss'],
})
export class AgendaListComponent implements OnInit {
  CalendarView = CalendarView;
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();
  selectedDate!: Date;
  selectedEvent!: Agenda;
  showModal = false; // Controle da modal

  
  events: CalendarEvent[] = [];

  constructor(private agendaService: AgendaService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    // Chama o serviço para obter todas as agendas do banco
    this.agendaService.findAll().subscribe((agendaList) => {
      // Limpa os eventos existentes
      this.events = [];

      // Preenche os eventos com os dados da agenda
      for (let agenda of agendaList) {
        console.log(agenda.data)
        this.events.push({
          start: new Date(agenda.data), // A data da agenda
          title: agenda.descricao, // A descrição da agenda
          color: { primary: '#000000', secondary: '#D1D1D1' }, // Cor preta para todos os eventos
        });
      }
    });
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  goToPreviousMonth() {
    const newDate = new Date(this.viewDate);
    newDate.setMonth(this.viewDate.getMonth() - 1);
    this.viewDate = newDate;
  }

  goToNextMonth() {
    const newDate = new Date(this.viewDate);
    newDate.setMonth(this.viewDate.getMonth() + 1);
    this.viewDate = newDate;
  }

  getCurrentMonthAndYear(): string {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long' };
    return this.viewDate.toLocaleDateString('pt-BR', options);
  }

  // Abre a modal ao clicar em um dia, para salvar um evento
  onDayClick(date: Date): void {
    this.viewDate = date;
    this.view = CalendarView.Day;
  }
  

  onHourSegmentClick(date: Date): void {
    this.selectedDate = new Date(date);
    this.selectedEvent = undefined as any;
    this.showModal = true;
    console.log("Clicou em hora/segmento: ", this.selectedDate);
  }
  
  
  //Abre a modal ao clicar em um evento já criado para edita-lo
  onEventClick(event: any): void {
    this.selectedDate = new Date(event.start);
    this.selectedEvent = event;
    this.showModal = true;
  }

  // Fecha a modal
  closeModal(): void {
    this.showModal = false;
  }
}
