import {Player} from "./Player.model";

export interface Message {
  id: number;
  creationDate: string;
  content: string;
  author: Player;
}
