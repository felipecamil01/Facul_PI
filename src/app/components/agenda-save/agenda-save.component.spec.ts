import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgendaSaveComponent } from './agenda-save.component';

describe('AgendaSaveComponent', () => {
  let component: AgendaSaveComponent;
  let fixture: ComponentFixture<AgendaSaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgendaSaveComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgendaSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
