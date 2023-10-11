package be.vandenn3.quiestce.service.dto;

import jakarta.validation.constraints.NotNull;

public class NewMessageDTO {

    @NotNull
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
