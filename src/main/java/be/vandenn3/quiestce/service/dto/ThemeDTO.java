package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.vandenn3.quiestce.domain.Theme} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private PictureDTO main_picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureDTO getMain_picture() {
        return main_picture;
    }

    public void setMain_picture(PictureDTO main_picture) {
        this.main_picture = main_picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeDTO)) {
            return false;
        }

        ThemeDTO themeDTO = (ThemeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, themeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", main_picture=" + getMain_picture() +
            "}";
    }
}
