import {Component} from '@angular/core';
import {GameService} from "../../service/game.service";
import {GameBoardComponent} from "../game-board/game-board.component";
import {CommonModule} from "@angular/common";
import {Subscription} from "rxjs";
import {GameCardComponent} from "../game-card/game-card.component";

@Component({
  standalone: true,
  selector: 'running-game',
  templateUrl: './running-game.component.html',
  imports: [
    GameBoardComponent,
    CommonModule,
    GameCardComponent
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
  protected isPlaying = false;
  public constructor(private gameService: GameService) {}

  protected clickOnCardHandler(gameCardId: number): void {
    const index = this.chosenCardsIds.findIndex(cardId => cardId === gameCardId);
    if (index !== -1) {
      this.chosenCardsIds.splice(index, 1);
    } else {
      this.chosenCardsIds.push(gameCardId);
    }
  }

  protected play(): void {
    if (this.isPlaying) {return;}
    this.isPlaying = true;
    this.playSubscription.unsubscribe();
    // a player can choose 0 cards.
    this.playSubscription = this.gameService.play$(this.chosenCardsIds).subscribe(() => {
      this.chosenCardsIds = [];
      this.isPlaying = false;
    }, () => {
      this.isPlaying = false;
    });

  }
}
