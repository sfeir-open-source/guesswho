import { TestBed } from '@angular/core/testing';

import { GameService } from './game.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('GameService', () => {
  let service: GameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(GameService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
