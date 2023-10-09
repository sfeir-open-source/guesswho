package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.vandenn3.quiestce.domain.ThemeCard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeCardDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ThemeDTO theme;

    private PictureDTO picture;

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

    public ThemeDTO getTheme() {
        return theme;
    }

    public void setTheme(ThemeDTO theme) {
        this.theme = theme;
    }

    public PictureDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureDTO picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeCardDTO)) {
            return false;
        }

        ThemeCardDTO themeCardDTO = (ThemeCardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, themeCardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeCardDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", theme=" + getTheme() +
            ", picture=" + getPicture() +
            "}";
    }
}
