import { TestBed } from '@angular/core/testing';

import { ThemeService } from './theme.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ThemeService', () => {
  let service: ThemeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ThemeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
