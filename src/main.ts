import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { APP_INITIALIZER } from '@angular/core';

import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { KeycloakService } from './app/auth/keycloak-service';
import { authInterceptor } from './app/auth/auth-interceptor';

export function kcFactory(kcService: KeycloakService) {
  return () => kcService.init();
}

bootstrapApplication(AppComponent, {
  providers: [
    KeycloakService,
    {
      provide: APP_INITIALIZER,
      useFactory: kcFactory,
      deps: [KeycloakService],
      multi: true,
    },
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
});
