import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import {RoomComponent} from "./page/room/room.component";
import {NewRoomComponent} from "./page/new-room/new-room.component";
import {NewGameComponent} from "./page/new-game/new-game.component";
import {GameComponent} from "./page/game/game.component";
import {JoinRoomComponent} from "./page/join-room/join-room.component";
import HomeComponent from "../home/home.component";
import {authGuard} from "../guard/auth.guard";

@NgModule({
  imports: [
    RouterModule.forChild(
      [
        {
          path: 'room',
          component: HomeComponent,
          pathMatch: 'full'
        },
        {
          path: 'room/new',
          component: NewRoomComponent,
          canActivate: [authGuard],
        },
        {
          path: 'room/join',
          component: JoinRoomComponent,
          canActivate: [authGuard],
        },
        {
          path: 'room/:roomId',
          component: RoomComponent,
          canActivate: [authGuard],
        },
        {
          path: 'room/:roomId/new-game',
          component: NewGameComponent,
          canActivate: [authGuard],
        },
        {
          path: 'room/:roomId/game/:gameId',
          component: GameComponent,
          canActivate: [authGuard],
        },
        {
          path: 'access-denied',
          component: HomeComponent,
        },
      ],
    ),
  ],
  exports: [RouterModule],
})
export class QuiEstCeRoutingModule {}
