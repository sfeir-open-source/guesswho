import {Player} from "./Player.model";

export interface Room {
  id: number;
  name: string;
  code: string;
  player1: Player;
  player2: Player;
}
