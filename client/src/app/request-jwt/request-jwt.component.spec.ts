import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestJwtComponent } from './request-jwt.component';

describe('RequestJwtComponent', () => {
  let component: RequestJwtComponent;
  let fixture: ComponentFixture<RequestJwtComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequestJwtComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestJwtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
