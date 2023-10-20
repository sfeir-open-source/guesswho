import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import {RoomComponent} from "./feature/room/room.component";

@NgModule({
  imports: [
    RouterModule.forChild(
      [
        {
          path: 'room',
          redirectTo: '', // TODO does not work...
          pathMatch: 'full'
        },
        {
          path: 'room/:id',
          component: RoomComponent,
        },
        {
          path: 'game/:id',
          component: RoomComponent,
        },
      ],
    ),
  ],
  exports: [RouterModule],
})
export class QuiEstCeRoutingModule {}
