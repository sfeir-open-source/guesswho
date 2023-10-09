package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.UserAnonymous;
import be.vandenn3.quiestce.repository.UserAnonymousRepository;
import be.vandenn3.quiestce.service.dto.UserAnonymousDTO;
import be.vandenn3.quiestce.service.mapper.UserAnonymousMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link be.vandenn3.quiestce.domain.UserAnonymous}.
 */
@Service
@Transactional
public class UserAnonymousService {

    private final Logger log = LoggerFactory.getLogger(UserAnonymousService.class);

    private final UserAnonymousRepository userAnonymousRepository;

    private final UserAnonymousMapper userAnonymousMapper;

    public UserAnonymousService(UserAnonymousRepository userAnonymousRepository, UserAnonymousMapper userAnonymousMapper) {
        this.userAnonymousRepository = userAnonymousRepository;
        this.userAnonymousMapper = userAnonymousMapper;
    }

    /**
     * Save a userAnonymous.
     *
     * @param userAnonymousDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAnonymousDTO save(UserAnonymousDTO userAnonymousDTO) {
        log.debug("Request to save UserAnonymous : {}", userAnonymousDTO);
        UserAnonymous userAnonymous = userAnonymousMapper.toEntity(userAnonymousDTO);
        userAnonymous = userAnonymousRepository.save(userAnonymous);
        return userAnonymousMapper.toDto(userAnonymous);
    }

    /**
     * Update a userAnonymous.
     *
     * @param userAnonymousDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAnonymousDTO update(UserAnonymousDTO userAnonymousDTO) {
        log.debug("Request to update UserAnonymous : {}", userAnonymousDTO);
        UserAnonymous userAnonymous = userAnonymousMapper.toEntity(userAnonymousDTO);
        userAnonymous = userAnonymousRepository.save(userAnonymous);
        return userAnonymousMapper.toDto(userAnonymous);
    }

    /**
     * Partially update a userAnonymous.
     *
     * @param userAnonymousDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserAnonymousDTO> partialUpdate(UserAnonymousDTO userAnonymousDTO) {
        log.debug("Request to partially update UserAnonymous : {}", userAnonymousDTO);

        return userAnonymousRepository
            .findById(userAnonymousDTO.getId())
            .map(existingUserAnonymous -> {
                userAnonymousMapper.partialUpdate(existingUserAnonymous, userAnonymousDTO);

                return existingUserAnonymous;
            })
            .map(userAnonymousRepository::save)
            .map(userAnonymousMapper::toDto);
    }

    /**
     * Get all the userAnonymous.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserAnonymousDTO> findAll() {
        log.debug("Request to get all UserAnonymous");
        return userAnonymousRepository.findAll().stream().map(userAnonymousMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userAnonymous by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserAnonymousDTO> findOne(Long id) {
        log.debug("Request to get UserAnonymous : {}", id);
        return userAnonymousRepository.findById(id).map(userAnonymousMapper::toDto);
    }

    /**
     * Delete the userAnonymous by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserAnonymous : {}", id);
        userAnonymousRepository.deleteById(id);
    }
}
