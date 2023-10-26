import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRoomComponent } from './new-room.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {IconsModule} from "../../../shared/test/icons-test.module";

describe('NewRoomComponent', () => {
  let component: NewRoomComponent;
  let fixture: ComponentFixture<NewRoomComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NewRoomComponent, HttpClientTestingModule, IconsModule],
    });
    fixture = TestBed.createComponent(NewRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
