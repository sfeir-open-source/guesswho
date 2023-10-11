package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.service.ThemeService;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeResource {
    private final ThemeService themeService;

    public ThemeResource(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("")
    public List<ThemeDTO> getThemes() {
        return themeService.getAll();
    }

    @GetMapping("/{id}")
    public List<ThemeCardDTO> getThemeDetails(@PathVariable Long id) {
        return themeService.getDetails(id);
    }
}
