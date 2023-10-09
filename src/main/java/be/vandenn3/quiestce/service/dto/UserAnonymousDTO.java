package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.vandenn3.quiestce.domain.UserAnonymous} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAnonymousDTO implements Serializable {

    private Long id;

    @NotNull
    private String pseudo;

    @NotNull
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAnonymousDTO)) {
            return false;
        }

        UserAnonymousDTO userAnonymousDTO = (UserAnonymousDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAnonymousDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAnonymousDTO{" +
            "id=" + getId() +
            ", pseudo='" + getPseudo() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }
}
