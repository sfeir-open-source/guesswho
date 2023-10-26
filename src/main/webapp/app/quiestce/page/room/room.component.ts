import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, RouterLink} from "@angular/router";
import {Observable, switchMap} from "rxjs";
import {RoomsService} from "../../service/rooms.service";
import {Room} from "../../domain/Room.model";
import {Game} from "../../domain/Game.model";
import {CommonModule} from "@angular/common";
import {map, tap} from "rxjs/operators";

@Component({
  standalone: true,
  selector: 'jhi-room',
  templateUrl: './room.component.html',
  imports: [
    RouterLink,
    CommonModule
  ],
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  room$: Observable<Room> | null = null;
  games$: Observable<Game[]> | null = null;
  canStartNewGame$: Observable<boolean> | null = null;
  player1WinsAmount$: Observable<number> | null = null;
  player2WinsAmount$: Observable<number> | null = null;
  private roomId: number | null = null;

  constructor(private route: ActivatedRoute, private roomsService: RoomsService) {
  }

  ngOnInit(): void {
    this.room$ = this.route.params.pipe(switchMap((params: Params) => {
      this.roomId = parseInt(params["roomId"], 10);
      this.games$ = this.roomsService.getGames$(this.roomId).pipe(tap(games => {
        games.sort((game1, game2) => game2.id - game1.id)
      }));
      this.canStartNewGame$ = this.games$.pipe(map(games => games.filter(game => !game.winner).length===0))
      return this.roomsService.getRoom$(this.roomId).pipe(tap(room => {
        this.player1WinsAmount$ = this.games$!.pipe(map(games => games.filter(game => game.winner?.id===room.player1.id).length))
        this.player2WinsAmount$ = this.games$!.pipe(map(games => games.filter(game => game.winner?.id===room.player2.id).length))
      }));
    }));
  }
}
