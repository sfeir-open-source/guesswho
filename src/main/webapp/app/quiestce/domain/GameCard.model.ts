import {ThemeCard} from "./ThemeCard.model";

export interface GameCard {
  id: number;
  player1_chosen: boolean;
  player2_chosen: boolean;
  player1_discarded: boolean;
  player2_discarded: boolean;
  themeCard: ThemeCard;
}
