package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.NotNull;

public class GameCreationDTO {

    @NotNull
    private Long roomId;
    @NotNull
    private Long themeId;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
}
