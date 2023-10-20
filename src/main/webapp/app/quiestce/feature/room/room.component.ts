import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {Observable, Subscription, switchMap} from "rxjs";
import {RoomsService} from "../../service/rooms.service";
import {Room} from "../../domain/Room.model";
import {GameService} from "../../service/game.service";
import {Game} from "../../domain/Game.model";

@Component({
  selector: 'jhi-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  private roomId: number | null = null;
  room$: Observable<Room> | null = null;
  games$: Observable<Game[]> | null = null;
  constructor(private route: ActivatedRoute, private roomsService: RoomsService, private gameService: GameService) {
  }

  ngOnInit() {
    this.room$ = this.route.params.pipe(switchMap((params: Params) => {
      this.roomId = parseInt(params["id"]);
      this.games$ = this.gameService.getGames$(this.roomId!);
      return this.roomsService.getRoom$(this.roomId!);
    }));
  }
}
