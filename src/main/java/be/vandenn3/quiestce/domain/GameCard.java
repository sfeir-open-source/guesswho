package be.vandenn3.quiestce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GameCard.
 */
@Entity
@Table(name = "game_card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameCard implements Serializable {

    private static final long serialVersionUID = 1L;

    public GameCard(Game game, ThemeCard themeCard) {
        this.game = game;
        this.themeCard = themeCard;
        this.player1_chosen = false;
        this.player2_chosen = false;
        this.player1_discarded = false;
        this.player2_discarded = false;
    }
    public GameCard() {};

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "player_1_chosen", nullable = false)
    private Boolean player1_chosen;

    @NotNull
    @Column(name = "player_2_chosen", nullable = false)
    private Boolean player2_chosen;

    @NotNull
    @Column(name = "player_1_discarded", nullable = false)
    private Boolean player1_discarded;

    @NotNull
    @Column(name = "player_2_discarded", nullable = false)
    private Boolean player2_discarded;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "room", "theme", "winner", "nextTurn" }, allowSetters = true)
    private Game game;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "theme", "picture" }, allowSetters = true)
    private ThemeCard themeCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameCard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPlayer1_chosen() {
        return this.player1_chosen;
    }

    public GameCard player1_chosen(Boolean player1_chosen) {
        this.setPlayer1_chosen(player1_chosen);
        return this;
    }

    public void setPlayer1_chosen(Boolean player1_chosen) {
        this.player1_chosen = player1_chosen;
    }

    public Boolean getPlayer2_chosen() {
        return this.player2_chosen;
    }

    public GameCard player2_chosen(Boolean player2_chosen) {
        this.setPlayer2_chosen(player2_chosen);
        return this;
    }

    public void setPlayer2_chosen(Boolean player2_chosen) {
        this.player2_chosen = player2_chosen;
    }

    public Boolean getPlayer1_discarded() {
        return this.player1_discarded;
    }

    public GameCard player1_discarded(Boolean player1_discarded) {
        this.setPlayer1_discarded(player1_discarded);
        return this;
    }

    public void setPlayer1_discarded(Boolean player1_discarded) {
        this.player1_discarded = player1_discarded;
    }

    public Boolean getPlayer2_discarded() {
        return this.player2_discarded;
    }

    public GameCard player2_discarded(Boolean player2_discarded) {
        this.setPlayer2_discarded(player2_discarded);
        return this;
    }

    public void setPlayer2_discarded(Boolean player2_discarded) {
        this.player2_discarded = player2_discarded;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameCard game(Game game) {
        this.setGame(game);
        return this;
    }

    public ThemeCard getThemeCard() {
        return this.themeCard;
    }

    public void setThemeCard(ThemeCard themeCard) {
        this.themeCard = themeCard;
    }

    public GameCard themeCard(ThemeCard themeCard) {
        this.setThemeCard(themeCard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public boolean hasPlayerDiscarded(int playerIndex) {
        if(playerIndex == 1) {
            return this.player1_discarded;
        } else if (playerIndex == 2) {
            return this.player2_discarded;
        } else {
            throw new IllegalStateException("current player not part of the game");
        }
    }

    public boolean hasPlayerChosen(int playerIndex) {
        if(playerIndex == 1) {
            return this.player1_chosen;
        } else if (playerIndex == 2) {
            return this.player2_chosen;
        } else {
            throw new IllegalStateException("current player not part of the game");
        }
    }

    public void setPlayerDiscarded(int playerIndex, boolean value) {
        if(playerIndex == 1) {
            this.player1_discarded = value;
        } else if (playerIndex == 2) {
            this.player2_discarded = value;
        } else {
            throw new IllegalStateException("current player not part of the game");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameCard)) {
            return false;
        }
        return getId() != null && getId().equals(((GameCard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCard{" +
            "id=" + getId() +
            ", player1_chosen='" + getPlayer1_chosen() + "'" +
            ", player2_chosen='" + getPlayer2_chosen() + "'" +
            ", player1_discarded='" + getPlayer1_discarded() + "'" +
            ", player2_discarded='" + getPlayer2_discarded() + "'" +
            "}";
    }
}
