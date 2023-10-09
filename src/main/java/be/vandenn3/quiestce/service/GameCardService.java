package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.GameCard;
import be.vandenn3.quiestce.repository.GameCardRepository;
import be.vandenn3.quiestce.service.dto.GameCardDTO;
import be.vandenn3.quiestce.service.mapper.GameCardMapper;
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
 * Service Implementation for managing {@link be.vandenn3.quiestce.domain.GameCard}.
 */
@Service
@Transactional
public class GameCardService {

    private final Logger log = LoggerFactory.getLogger(GameCardService.class);

    private final GameCardRepository gameCardRepository;

    private final GameCardMapper gameCardMapper;

    public GameCardService(GameCardRepository gameCardRepository, GameCardMapper gameCardMapper) {
        this.gameCardRepository = gameCardRepository;
        this.gameCardMapper = gameCardMapper;
    }

    /**
     * Save a gameCard.
     *
     * @param gameCardDTO the entity to save.
     * @return the persisted entity.
     */
    public GameCardDTO save(GameCardDTO gameCardDTO) {
        log.debug("Request to save GameCard : {}", gameCardDTO);
        GameCard gameCard = gameCardMapper.toEntity(gameCardDTO);
        gameCard = gameCardRepository.save(gameCard);
        return gameCardMapper.toDto(gameCard);
    }

    /**
     * Update a gameCard.
     *
     * @param gameCardDTO the entity to save.
     * @return the persisted entity.
     */
    public GameCardDTO update(GameCardDTO gameCardDTO) {
        log.debug("Request to update GameCard : {}", gameCardDTO);
        GameCard gameCard = gameCardMapper.toEntity(gameCardDTO);
        gameCard = gameCardRepository.save(gameCard);
        return gameCardMapper.toDto(gameCard);
    }

    /**
     * Partially update a gameCard.
     *
     * @param gameCardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GameCardDTO> partialUpdate(GameCardDTO gameCardDTO) {
        log.debug("Request to partially update GameCard : {}", gameCardDTO);

        return gameCardRepository
            .findById(gameCardDTO.getId())
            .map(existingGameCard -> {
                gameCardMapper.partialUpdate(existingGameCard, gameCardDTO);

                return existingGameCard;
            })
            .map(gameCardRepository::save)
            .map(gameCardMapper::toDto);
    }

    /**
     * Get all the gameCards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GameCardDTO> findAll() {
        log.debug("Request to get all GameCards");
        return gameCardRepository.findAll().stream().map(gameCardMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the gameCards with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GameCardDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gameCardRepository.findAllWithEagerRelationships(pageable).map(gameCardMapper::toDto);
    }

    /**
     * Get one gameCard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GameCardDTO> findOne(Long id) {
        log.debug("Request to get GameCard : {}", id);
        return gameCardRepository.findOneWithEagerRelationships(id).map(gameCardMapper::toDto);
    }

    /**
     * Delete the gameCard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameCard : {}", id);
        gameCardRepository.deleteById(id);
    }
}
