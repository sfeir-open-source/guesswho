import {Component, OnDestroy, OnInit} from '@angular/core';
import {GameService} from "./service/game.service";
import {GameBoardComponent} from "./component/game-board/game-board.component";
import {map} from "rxjs/operators";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {combineLatest, Subscription} from "rxjs";
import {PlayerService} from "../../service/player.service";
import {AsyncPipe, NgIf} from "@angular/common";
import {SelectOwnCardComponent} from "./component/select-own-card/select-own-card.component";
import {RunningGameComponent} from "./component/running-game/running-game.component";
import {ChatComponent} from "./component/chat/chat.component";

@Component({
  standalone: true,
  selector: 'jhi-game',
  templateUrl: './game.component.html',
  imports: [
    GameBoardComponent,
    NgIf,
    AsyncPipe,
    SelectOwnCardComponent,
    RunningGameComponent,
    RouterLink,
    ChatComponent
  ],
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  protected shouldSelectCard$ = this.gameService.shouldSelectFirstCard$;
  protected isWaitingForOtherPlayerToChooseCard$ = this.gameService.isWaitingForOtherPlayerToChooseCard$;
  protected hasGameStarted$ = this.gameService.hasGameStarted$;

  private subscriptions = new Subscription();
  private refreshIntervalRef = 0;
  private currentGameId: number | undefined = undefined;
  private currentPlayerId: number = -1;

  public constructor(private gameService: GameService, private route: ActivatedRoute,
                     private playerService: PlayerService) {}

  ngOnInit() {
    this.subscriptions.add(
      combineLatest([this.route.params, this.playerService.getCurrentPlayerId$()])
        .pipe(map(([params, currentPlayerId]) => {
          this.initGame(parseInt(params["roomId"], 10), parseInt(params["gameId"], 10), currentPlayerId);
    })).subscribe());
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
    clearInterval(this.refreshIntervalRef);
  }

  private initGame(roomId: number, gameId: number, currentPlayerId: number) {
    this.currentPlayerId = currentPlayerId;
    if (gameId !== this.currentGameId) {
      this.currentGameId = gameId;
      this.gameService.setGame(roomId, gameId, this.currentPlayerId);
    }
    this.gameService.updateDataFromServer();
    // @ts-ignore
    this.refreshIntervalRef = setInterval(() => this.gameService.updateDataFromServer(), 2000);
  }
}
