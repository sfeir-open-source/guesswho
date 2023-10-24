import {Component, OnDestroy} from '@angular/core';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RoomsService} from "../../service/rooms.service";
import {catchError, map} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {EMPTY, Subscription} from "rxjs";
import {NgIf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'jhi-join-room',
  templateUrl: './join-room.component.html',
  imports: [
    FontAwesomeModule,
    FormsModule,
    ReactiveFormsModule,
    NgIf
  ],
  styleUrls: ['./join-room.component.scss']
})
export class JoinRoomComponent implements OnDestroy{
  public roomCode = "";
  public isCodeInvalid = false;
  private subscriptions = new Subscription();

  public constructor(private roomsService: RoomsService, private route: ActivatedRoute, private router: Router) {
  }

  public joinRoom() {
    this.subscriptions.add(this.roomsService.joinRoom$(this.roomCode).pipe(catchError(() => {
      this.isCodeInvalid = true;
      return EMPTY;
    }), map(room => {
      this.isCodeInvalid = false;
      return this.router.navigate(["/room", room.id]);
    })).subscribe());
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
