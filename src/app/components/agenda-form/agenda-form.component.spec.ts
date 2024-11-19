import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgendaFormComponent } from './agenda-form.component';

describe('AgendaSaveComponent', () => {
  let component: AgendaFormComponent;
  let fixture: ComponentFixture<AgendaFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgendaFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgendaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
