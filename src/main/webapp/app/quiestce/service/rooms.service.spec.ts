import { TestBed } from '@angular/core/testing';

import { RoomsService } from './rooms.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('RoomsServiceService', () => {
  let service: RoomsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(RoomsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
