import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessoComponent } from './processo.component';

describe('ProcessoComponent', () => {
  let component: ProcessoComponent;
  let fixture: ComponentFixture<ProcessoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
