package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.NotNull;

public class RoomCreationDTO {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
