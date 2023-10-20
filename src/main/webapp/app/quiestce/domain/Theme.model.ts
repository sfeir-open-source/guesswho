import {Picture} from "./Picture.model";

export interface Theme {
  id: number;
  name: string;
  main_picture: Picture;
}
