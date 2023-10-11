package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.NotNull;

public class SelectCardDTO {

    @NotNull
    private Long gameCardId;

    public Long getGameCardId() {
        return gameCardId;
    }

    public void setGameCardId(Long gameCardId) {
        this.gameCardId = gameCardId;
    }
}
