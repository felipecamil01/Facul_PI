import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import { Injectable } from '@angular/core';
import { format } from 'date-fns';
import ptBR from 'date-fns/locale/pt-BR';

@Injectable()
export class CustomDateFormatter extends CalendarDateFormatter {
  override monthViewColumnHeader({ date }: DateFormatterParams): string {
    return format(date, 'EEEE', { locale: ptBR }); // segunda, terça...
  }

  override monthViewTitle({ date }: DateFormatterParams): string {
    return format(date, "MMMM yyyy", { locale: ptBR }); // abril 2025
  }

  override weekViewTitle({ date }: DateFormatterParams): string {
    return format(date, "'Semana de' dd MMMM yyyy", { locale: ptBR });
  }

  override weekViewHour({ date }: DateFormatterParams): string {
    return format(date, 'HH:mm', { locale: ptBR }); // 24h
  }

  override dayViewTitle({ date }: DateFormatterParams): string {
    return format(date, "EEEE, dd MMMM yyyy", { locale: ptBR });
  }

  override dayViewHour({ date }: DateFormatterParams): string {
    return format(date, 'HH:mm', { locale: ptBR }); // 24h
  }
  
}
