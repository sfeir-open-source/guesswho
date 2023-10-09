import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';

@Component({
  standalone: true,
  templateUrl: './room-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoomDeleteDialogComponent {
  room?: IRoom;

  constructor(
    protected roomService: RoomService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
