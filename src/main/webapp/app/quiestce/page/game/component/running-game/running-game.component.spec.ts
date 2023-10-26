import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RunningGameComponent} from './running-game.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('RunningGameComponent', () => {
  let component: RunningGameComponent;
  let fixture: ComponentFixture<RunningGameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RunningGameComponent, HttpClientTestingModule]
    });
    fixture = TestBed.createComponent(RunningGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
