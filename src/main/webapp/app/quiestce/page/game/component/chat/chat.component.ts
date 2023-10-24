import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {GameService} from "../../service/game.service";
import {ActivatedRoute} from "@angular/router";
import {PlayerService} from "../../../../service/player.service";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'chat',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  protected newMessage = "";
  protected messages$ = this.gameService.messages$;
  protected sending = false;
  protected currentPlayer$ = this.playerService.getCurrentPlayerId$();
  public constructor(private gameService: GameService, private playerService: PlayerService) {}

  ngOnInit() {
    this.messages$.subscribe(() => {
      setTimeout(() => {
        const div = document.getElementById("messages-list");
        if (div) div.scrollTop = div.scrollHeight;
      }, 20);
    })
  }

  protected sendMessage() {
    if (this.sending || this.newMessage.trim() === '') return;
    this.sending = true;
    this.gameService.sendMessage$(this.newMessage).subscribe(() => {
      this.sending = false;
      this.newMessage = "";
    }, () => {
      this.sending = false;
    });
  }
}
