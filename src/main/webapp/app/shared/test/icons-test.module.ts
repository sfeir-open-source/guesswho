import {FaIconLibrary, FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {NgModule} from "@angular/core";
import {fas} from "@fortawesome/free-solid-svg-icons";

@NgModule({
  imports: [ FontAwesomeModule ],
  exports: [ FontAwesomeModule ],
  bootstrap: [],
})
export class IconsModule {
  constructor(library: FaIconLibrary) {
    library.addIconPacks(fas);
  }
}
