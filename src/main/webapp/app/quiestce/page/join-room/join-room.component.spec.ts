import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinRoomComponent } from './join-room.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {IconsModule} from "../../../shared/test/icons-test.module";

describe('JoinRoomComponent', () => {
  let component: JoinRoomComponent;
  let fixture: ComponentFixture<JoinRoomComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [JoinRoomComponent, HttpClientTestingModule, RouterTestingModule, IconsModule]
    });
    fixture = TestBed.createComponent(JoinRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
