package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.repository.ThemeCardRepository;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import be.vandenn3.quiestce.service.mapper.ThemeCardMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link be.vandenn3.quiestce.domain.ThemeCard}.
 */
@Service
@Transactional
public class ThemeCardService {

    private final Logger log = LoggerFactory.getLogger(ThemeCardService.class);

    private final ThemeCardRepository themeCardRepository;

    private final ThemeCardMapper themeCardMapper;

    public ThemeCardService(ThemeCardRepository themeCardRepository, ThemeCardMapper themeCardMapper) {
        this.themeCardRepository = themeCardRepository;
        this.themeCardMapper = themeCardMapper;
    }

    /**
     * Save a themeCard.
     *
     * @param themeCardDTO the entity to save.
     * @return the persisted entity.
     */
    public ThemeCardDTO save(ThemeCardDTO themeCardDTO) {
        log.debug("Request to save ThemeCard : {}", themeCardDTO);
        ThemeCard themeCard = themeCardMapper.toEntity(themeCardDTO);
        themeCard = themeCardRepository.save(themeCard);
        return themeCardMapper.toDto(themeCard);
    }

    /**
     * Update a themeCard.
     *
     * @param themeCardDTO the entity to save.
     * @return the persisted entity.
     */
    public ThemeCardDTO update(ThemeCardDTO themeCardDTO) {
        log.debug("Request to update ThemeCard : {}", themeCardDTO);
        ThemeCard themeCard = themeCardMapper.toEntity(themeCardDTO);
        themeCard = themeCardRepository.save(themeCard);
        return themeCardMapper.toDto(themeCard);
    }

    /**
     * Partially update a themeCard.
     *
     * @param themeCardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ThemeCardDTO> partialUpdate(ThemeCardDTO themeCardDTO) {
        log.debug("Request to partially update ThemeCard : {}", themeCardDTO);

        return themeCardRepository
            .findById(themeCardDTO.getId())
            .map(existingThemeCard -> {
                themeCardMapper.partialUpdate(existingThemeCard, themeCardDTO);

                return existingThemeCard;
            })
            .map(themeCardRepository::save)
            .map(themeCardMapper::toDto);
    }

    /**
     * Get all the themeCards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeCardDTO> findAll() {
        log.debug("Request to get all ThemeCards");
        return themeCardRepository.findAll().stream().map(themeCardMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the themeCards with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ThemeCardDTO> findAllWithEagerRelationships(Pageable pageable) {
        return themeCardRepository.findAllWithEagerRelationships(pageable).map(themeCardMapper::toDto);
    }

    /**
     * Get one themeCard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ThemeCardDTO> findOne(Long id) {
        log.debug("Request to get ThemeCard : {}", id);
        return themeCardRepository.findOneWithEagerRelationships(id).map(themeCardMapper::toDto);
    }

    /**
     * Delete the themeCard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ThemeCard : {}", id);
        themeCardRepository.deleteById(id);
    }
}
