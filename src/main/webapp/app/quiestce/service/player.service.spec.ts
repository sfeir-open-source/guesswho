import { TestBed } from '@angular/core/testing';

import { PlayerService } from './player.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('PlayerService', () => {
  let service: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(PlayerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
