package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.RaceType;
import it.acsi.cycling.races.repository.RaceTypeRepository;
import it.acsi.cycling.races.repository.search.RaceTypeSearchRepository;
import it.acsi.cycling.races.service.dto.RaceTypeDTO;
import it.acsi.cycling.races.service.mapper.RaceTypeMapper;
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
 * Service Implementation for managing {@link RaceType}.
 */
@Service
@Transactional
public class RaceTypeService {

    private final Logger log = LoggerFactory.getLogger(RaceTypeService.class);

    private final RaceTypeRepository raceTypeRepository;

    private final RaceTypeMapper raceTypeMapper;

    private final RaceTypeSearchRepository raceTypeSearchRepository;

    public RaceTypeService(RaceTypeRepository raceTypeRepository, RaceTypeMapper raceTypeMapper, RaceTypeSearchRepository raceTypeSearchRepository) {
        this.raceTypeRepository = raceTypeRepository;
        this.raceTypeMapper = raceTypeMapper;
        this.raceTypeSearchRepository = raceTypeSearchRepository;
    }

    /**
     * Save a raceType.
     *
     * @param raceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceTypeDTO save(RaceTypeDTO raceTypeDTO) {
        log.debug("Request to save RaceType : {}", raceTypeDTO);
        RaceType raceType = raceTypeMapper.toEntity(raceTypeDTO);
        raceType = raceTypeRepository.save(raceType);
        RaceTypeDTO result = raceTypeMapper.toDto(raceType);
        raceTypeSearchRepository.save(raceType);
        return result;
    }

    /**
     * Get all the raceTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RaceTypeDTO> findAll() {
        log.debug("Request to get all RaceTypes");
        return raceTypeRepository.findAll().stream()
            .map(raceTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one raceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceTypeDTO> findOne(Long id) {
        log.debug("Request to get RaceType : {}", id);
        return raceTypeRepository.findById(id)
            .map(raceTypeMapper::toDto);
    }

    /**
     * Delete the raceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RaceType : {}", id);
        raceTypeRepository.deleteById(id);
        raceTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the raceType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RaceTypeDTO> search(String query) {
        log.debug("Request to search RaceTypes for query {}", query);
        return StreamSupport
            .stream(raceTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(raceTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
