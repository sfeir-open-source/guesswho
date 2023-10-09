package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.vandenn3.quiestce.domain.Picture} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PictureDTO implements Serializable {

    private Long id;

    @NotNull
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PictureDTO)) {
            return false;
        }

        PictureDTO pictureDTO = (PictureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pictureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PictureDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            "}";
    }
}
