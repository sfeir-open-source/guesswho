import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomsListComponent} from './rooms-list.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";

describe('RoomsListComponent', () => {
  let component: RoomsListComponent;
  let fixture: ComponentFixture<RoomsListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RoomsListComponent, HttpClientTestingModule, RouterTestingModule]
    });
    fixture = TestBed.createComponent(RoomsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
