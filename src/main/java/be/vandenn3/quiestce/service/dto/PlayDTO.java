package be.vandenn3.quiestce.service.dto;

import java.util.List;

public class PlayDTO {
    List<Long> gameCardIds;

    public List<Long> getGameCardIds() {
        return gameCardIds;
    }

    public void setGameCardIds(List<Long> gameCardIds) {
        this.gameCardIds = gameCardIds;
    }
}
