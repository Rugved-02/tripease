import { TestBed } from '@angular/core/testing';

import { hotelapiservice } from './hotelapiservice';

describe('Hotelapiservice', () => {
  let service: hotelapiservice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(hotelapiservice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
