import {Theme} from "./Theme.model";
import {Picture} from "./Picture.model";

export interface ThemeCard {
  id: number;
  name: string;
  theme: Theme;
  picture: Picture;
}
