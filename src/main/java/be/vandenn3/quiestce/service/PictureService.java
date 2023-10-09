package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.Picture;
import be.vandenn3.quiestce.repository.PictureRepository;
import be.vandenn3.quiestce.service.dto.PictureDTO;
import be.vandenn3.quiestce.service.mapper.PictureMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link be.vandenn3.quiestce.domain.Picture}.
 */
@Service
@Transactional
public class PictureService {

    private final Logger log = LoggerFactory.getLogger(PictureService.class);

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    public PictureService(PictureRepository pictureRepository, PictureMapper pictureMapper) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
    }

    /**
     * Save a picture.
     *
     * @param pictureDTO the entity to save.
     * @return the persisted entity.
     */
    public PictureDTO save(PictureDTO pictureDTO) {
        log.debug("Request to save Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

    /**
     * Update a picture.
     *
     * @param pictureDTO the entity to save.
     * @return the persisted entity.
     */
    public PictureDTO update(PictureDTO pictureDTO) {
        log.debug("Request to update Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

    /**
     * Partially update a picture.
     *
     * @param pictureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PictureDTO> partialUpdate(PictureDTO pictureDTO) {
        log.debug("Request to partially update Picture : {}", pictureDTO);

        return pictureRepository
            .findById(pictureDTO.getId())
            .map(existingPicture -> {
                pictureMapper.partialUpdate(existingPicture, pictureDTO);

                return existingPicture;
            })
            .map(pictureRepository::save)
            .map(pictureMapper::toDto);
    }

    /**
     * Get all the pictures.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PictureDTO> findAll() {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll().stream().map(pictureMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one picture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PictureDTO> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id).map(pictureMapper::toDto);
    }

    /**
     * Delete the picture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
    }
}
