import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClienteEditarComponent } from './cliente-editar.component';

describe('ClienteEditarComponent', () => {
  let component: ClienteEditarComponent;
  let fixture: ComponentFixture<ClienteEditarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteEditarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClienteEditarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
