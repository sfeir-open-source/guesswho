package be.vandenn3.quiestce.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Room name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Room code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public void setPlayer1(Player player) {
        this.player1 = player;
    }

    public Room player1(Player player) {
        this.setPlayer1(player);
        return this;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public void setPlayer2(Player player) {
        this.player2 = player;
    }

    public Room player2(Player player) {
        this.setPlayer2(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public boolean isPlayer1(Player player) {
        return Objects.nonNull(player) && player.equals(this.getPlayer1());
    }

    public boolean isPlayer2(Player player) {
        return Objects.nonNull(player) && player.equals(this.getPlayer2());
    }

    public Player getOtherPlayer(Player player) {
        if (isPlayer1(player)) {
            return this.getPlayer2();
        } else if (isPlayer2(player)) {
            return this.getPlayer1();
        } else {
            throw new IllegalArgumentException("given player is not part of this game");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return getId() != null && getId().equals(((Room) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
