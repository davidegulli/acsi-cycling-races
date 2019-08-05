package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.PathType;
import it.acsi.cycling.races.repository.PathTypeRepository;
import it.acsi.cycling.races.repository.search.PathTypeSearchRepository;
import it.acsi.cycling.races.service.dto.PathTypeDTO;
import it.acsi.cycling.races.service.mapper.PathTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PathType}.
 */
@Service
@Transactional
public class PathTypeService {

    private final Logger log = LoggerFactory.getLogger(PathTypeService.class);

    private final PathTypeRepository pathTypeRepository;

    private final PathTypeMapper pathTypeMapper;

    private final PathTypeSearchRepository pathTypeSearchRepository;

    public PathTypeService(PathTypeRepository pathTypeRepository, PathTypeMapper pathTypeMapper, PathTypeSearchRepository pathTypeSearchRepository) {
        this.pathTypeRepository = pathTypeRepository;
        this.pathTypeMapper = pathTypeMapper;
        this.pathTypeSearchRepository = pathTypeSearchRepository;
    }

    /**
     * Save a pathType.
     *
     * @param pathTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public PathTypeDTO save(PathTypeDTO pathTypeDTO) {
        log.debug("Request to save PathType : {}", pathTypeDTO);
        PathType pathType = pathTypeMapper.toEntity(pathTypeDTO);
        pathType = pathTypeRepository.save(pathType);
        PathTypeDTO result = pathTypeMapper.toDto(pathType);
        pathTypeSearchRepository.save(pathType);
        return result;
    }

    /**
     * Get all the pathTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PathTypeDTO> findAll() {
        log.debug("Request to get all PathTypes");
        return pathTypeRepository.findAll().stream()
            .map(pathTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pathType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PathTypeDTO> findOne(Long id) {
        log.debug("Request to get PathType : {}", id);
        return pathTypeRepository.findById(id)
            .map(pathTypeMapper::toDto);
    }

    /**
     * Delete the pathType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PathType : {}", id);
        pathTypeRepository.deleteById(id);
        pathTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the pathType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PathTypeDTO> search(String query) {
        log.debug("Request to search PathTypes for query {}", query);
        return StreamSupport
            .stream(pathTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(pathTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
