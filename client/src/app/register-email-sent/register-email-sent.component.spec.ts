import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterEmailSentComponent } from './register-email-sent.component';

describe('RegisterEmailSentComponent', () => {
  let component: RegisterEmailSentComponent;
  let fixture: ComponentFixture<RegisterEmailSentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterEmailSentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterEmailSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
