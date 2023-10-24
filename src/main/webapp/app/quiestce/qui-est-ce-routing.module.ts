import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import {RoomComponent} from "./page/room/room.component";
import {NewRoomComponent} from "./page/new-room/new-room.component";
import {NewGameComponent} from "./page/new-game/new-game.component";
import {GameComponent} from "./page/game/game.component";
import {JoinRoomComponent} from "./page/join-room/join-room.component";

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
          path: 'room/new',
          component: NewRoomComponent,
        },
        {
          path: 'room/join',
          component: JoinRoomComponent,
        },
        {
          path: 'room/:roomId',
          component: RoomComponent,
        },
        {
          path: 'room/:roomId/new-game',
          component: NewGameComponent,
        },
        {
          path: 'room/:roomId/game/:gameId',
          component: GameComponent,
        },
      ],
    ),
  ],
  exports: [RouterModule],
})
export class QuiEstCeRoutingModule {}
