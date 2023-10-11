package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.repository.ThemeCardRepository;
import be.vandenn3.quiestce.repository.ThemeRepository;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
import be.vandenn3.quiestce.service.mapper.ThemeCardMapper;
import be.vandenn3.quiestce.service.mapper.ThemeMapper;
import be.vandenn3.quiestce.web.rest.RoomResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThemeService {
    private final Logger log = LoggerFactory.getLogger(ThemeService.class);

    private final ThemeRepository themeRepository;
    private final ThemeCardRepository themeCardRepository;
    private final ThemeMapper themeMapper;
    private final ThemeCardMapper themeCardMapper;

    public ThemeService(ThemeRepository themeRepository, ThemeCardRepository themeCardRepository, ThemeMapper themeMapper, ThemeCardMapper themeCardMapper) {
        this.themeRepository = themeRepository;
        this.themeCardRepository = themeCardRepository;
        this.themeMapper = themeMapper;
        this.themeCardMapper = themeCardMapper;
    }

    public List<ThemeDTO> getAll() {
        return themeMapper.toDto(themeRepository.findAll());
    }

    public List<ThemeCardDTO> getDetails(Long themeId) {
        return themeCardMapper.toDto(themeCardRepository.findAllByThemeId(themeId));
    }
}
