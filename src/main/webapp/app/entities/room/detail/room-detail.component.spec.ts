import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RoomDetailComponent } from './room-detail.component';

describe('Room Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoomDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RoomDetailComponent,
              resolve: { room: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RoomDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load room on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RoomDetailComponent);

      // THEN
      expect(instance.room).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
