import { Component } from '@angular/core';
import {GameService} from "../../service/game.service";
import {GameBoardComponent} from "../game-board/game-board.component";
import {map} from "rxjs/operators";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {async, Subscription} from "rxjs";

@Component({
  standalone: true,
  selector: 'running-game',
  templateUrl: './running-game.component.html',
  imports: [
    GameBoardComponent,
    NgIf,
    AsyncPipe,
    NgForOf
  ],
  styleUrls: ['./running-game.component.scss']
})
export class RunningGameComponent {
  protected hasGameEnded$ = this.gameService.hasGameEnded$;
  protected disabledCardsIds$ = this.gameService.disabledCardsIds$;
  protected hasWon$ = this.gameService.hasWon$;
  protected isMyTurn$ = this.gameService.isMyTurn$;
  protected themeCards$ = this.gameService.themeCards$;
  protected gameCards$ = this.gameService.gameCards$;
  protected otherPlayerCard$ = this.gameService.otherPlayerCard$;
  protected cardsPaths$ = this.gameService.cardsPaths$;
  protected chosenCardsIds: number[] = [];
  protected playSubscription = new Subscription();
  public constructor(private gameService: GameService) {}

  protected clickOnCardHandler(gameCardId: number) {
    const index = this.chosenCardsIds.findIndex(cardId => cardId === gameCardId);
    if (index !== -1) {
      this.chosenCardsIds.splice(index, 1);
    } else {
      this.chosenCardsIds.push(gameCardId);
    }
  }

  protected play() {
    this.playSubscription.unsubscribe();
    // a player can choose 0 cards.
    // TODO prevent playing while request is in progress
    this.playSubscription = this.gameService.play$(this.chosenCardsIds).subscribe(() => {
      this.chosenCardsIds = [];
    });

  }
}
