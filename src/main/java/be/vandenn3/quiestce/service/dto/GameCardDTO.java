package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.vandenn3.quiestce.domain.GameCard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameCardDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean player1_chosen;

    @NotNull
    private Boolean player2_chosen;

    @NotNull
    private Boolean player1_discarded;

    @NotNull
    private Boolean player2_discarded;

    private GameDTO game;

    private ThemeCardDTO themeCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPlayer1_chosen() {
        return player1_chosen;
    }

    public void setPlayer1_chosen(Boolean player1_chosen) {
        this.player1_chosen = player1_chosen;
    }

    public Boolean getPlayer2_chosen() {
        return player2_chosen;
    }

    public void setPlayer2_chosen(Boolean player2_chosen) {
        this.player2_chosen = player2_chosen;
    }

    public Boolean getPlayer1_discarded() {
        return player1_discarded;
    }

    public void setPlayer1_discarded(Boolean player1_discarded) {
        this.player1_discarded = player1_discarded;
    }

    public Boolean getPlayer2_discarded() {
        return player2_discarded;
    }

    public void setPlayer2_discarded(Boolean player2_discarded) {
        this.player2_discarded = player2_discarded;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public ThemeCardDTO getThemeCard() {
        return themeCard;
    }

    public void setThemeCard(ThemeCardDTO themeCard) {
        this.themeCard = themeCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameCardDTO)) {
            return false;
        }

        GameCardDTO gameCardDTO = (GameCardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameCardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCardDTO{" +
            "id=" + getId() +
            ", player1_chosen='" + getPlayer1_chosen() + "'" +
            ", player2_chosen='" + getPlayer2_chosen() + "'" +
            ", player1_discarded='" + getPlayer1_discarded() + "'" +
            ", player2_discarded='" + getPlayer2_discarded() + "'" +
            ", game=" + getGame() +
            ", themeCard=" + getThemeCard() +
            "}";
    }
}
