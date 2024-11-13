import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DespesaListComponent } from './despesa-list.component';

describe('DespesaComponent', () => {
  let component: DespesaListComponent;
  let fixture: ComponentFixture<DespesaListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DespesaListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DespesaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
