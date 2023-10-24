import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectOwnCardComponent } from './select-own-card.component';

describe('SelectOwnCardComponent', () => {
  let component: SelectOwnCardComponent;
  let fixture: ComponentFixture<SelectOwnCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectOwnCardComponent]
    });
    fixture = TestBed.createComponent(SelectOwnCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
