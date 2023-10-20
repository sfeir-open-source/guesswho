import {Room} from "./Room.model";
import {Theme} from "./Theme.model";
import {Player} from "./Player.model";
import {GameCard} from "./GameCard.model";

export interface Game {
  id: number;
  startDate: string |null;
  endDate: string | null;
  room: Room;
  theme: Theme;
  winner: Player | null;
  nextTurn: Player | null;
  gameCards: GameCard[];
}
