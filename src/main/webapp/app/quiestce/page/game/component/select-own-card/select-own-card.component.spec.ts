import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectOwnCardComponent } from './select-own-card.component';
import {GameService} from "../../service/game.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('SelectOwnCardComponent', () => {
  let component: SelectOwnCardComponent;
  let fixture: ComponentFixture<SelectOwnCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SelectOwnCardComponent, HttpClientTestingModule],
    });
    fixture = TestBed.createComponent(SelectOwnCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
