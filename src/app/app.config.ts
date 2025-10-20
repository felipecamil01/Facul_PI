import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { meuhttpInterceptor } from './auth/http-interceptor.service';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DateAdapter, CalendarUtils, CalendarA11y, CalendarDateFormatter } from 'angular-calendar';
import { DatePipe } from '@angular/common';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(withInterceptors([meuhttpInterceptor])),
    provideAnimations(), provideAnimationsAsync(), provideAnimationsAsync(),
    // Providers required by angular-calendar when using standalone components
    { provide: DateAdapter, useFactory: adapterFactory },
    CalendarUtils,
    CalendarA11y,
    CalendarDateFormatter,
  DatePipe, provideAnimationsAsync(),
  ],
};
