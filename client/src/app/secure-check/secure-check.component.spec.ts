import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecureCheckComponent } from './secure-check.component';

describe('SecureCheckComponent', () => {
  let component: SecureCheckComponent;
  let fixture: ComponentFixture<SecureCheckComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecureCheckComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecureCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
