import { Component } from '@angular/core';
import {GameService} from "../../service/game.service";
import {map} from "rxjs/operators";
import {GameBoardComponent} from "../game-board/game-board.component";

@Component({
  standalone: true,
  selector: 'select-own-card',
  templateUrl: './select-own-card.component.html',
  imports: [
    GameBoardComponent
  ],
  styleUrls: ['./select-own-card.component.scss']
})
export class SelectOwnCardComponent {
  protected themeCards$ = this.gameService.themeCards$;
  protected gameCards$ = this.gameService.gameData$.pipe(map(gameData => gameData.gameCards));

  public constructor(private gameService: GameService) {}
  public clickOnCardHandler(gameCardId: number): void {
    this.gameService.selectMyCard$(gameCardId).subscribe();
  }
}
