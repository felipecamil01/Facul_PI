import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClienteSaveComponent } from './cliente-save.component';

describe('ClienteSaveComponent', () => {
  let component: ClienteSaveComponent;
  let fixture: ComponentFixture<ClienteSaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteSaveComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClienteSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
