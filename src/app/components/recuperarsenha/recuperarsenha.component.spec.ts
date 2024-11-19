import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecuperarsenhaComponent } from './recuperarsenha.component';

describe('RecuperarsenhaComponent', () => {
  let component: RecuperarsenhaComponent;
  let fixture: ComponentFixture<RecuperarsenhaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecuperarsenhaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecuperarsenhaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
