import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginEmailSentComponent } from './login-email-sent.component';

describe('LoginEmailSentComponent', () => {
  let component: LoginEmailSentComponent;
  let fixture: ComponentFixture<LoginEmailSentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginEmailSentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginEmailSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
