import {Component, OnDestroy} from '@angular/core';
import {RoomsService} from "../../service/rooms.service";
import {Subscription, switchMap} from "rxjs";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import SharedModule from "../../../shared/shared.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

@Component({
  standalone: true,
  selector: 'jhi-new-room',
  templateUrl: './new-room.component.html',
  imports: [
    FormsModule,
    SharedModule,
    FontAwesomeModule
  ],
  styleUrls: ['./new-room.component.scss']
})
export class NewRoomComponent implements OnDestroy {
  public newRoomName = "";
  private subscriptions = new Subscription();

  constructor(private roomsService: RoomsService, private router: Router) {
  }
  public createRoom(): void {
    if (this.newRoomName.trim() === "") {
      return;
    }
    this.subscriptions.add(this.roomsService.createRoom$(this.newRoomName).pipe(switchMap((room) => {
      this.newRoomName = "";
      return this.router.navigate(["/room", room.id]);
    })).subscribe());
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
