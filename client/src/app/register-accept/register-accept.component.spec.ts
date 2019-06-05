import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterAcceptComponent } from './register-accept.component';

describe('RegisterAcceptComponent', () => {
  let component: RegisterAcceptComponent;
  let fixture: ComponentFixture<RegisterAcceptComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterAcceptComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterAcceptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
