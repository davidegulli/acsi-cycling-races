package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.Race;
import it.acsi.cycling.races.repository.RaceRepository;
import it.acsi.cycling.races.repository.search.RaceSearchRepository;
import it.acsi.cycling.races.service.dto.RaceDTO;
import it.acsi.cycling.races.service.mapper.RaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Race}.
 */
@Service
@Transactional
public class RaceService {

    private final Logger log = LoggerFactory.getLogger(RaceService.class);

    private final RaceRepository raceRepository;

    private final RaceMapper raceMapper;

    private final RaceSearchRepository raceSearchRepository;

    public RaceService(RaceRepository raceRepository, RaceMapper raceMapper, RaceSearchRepository raceSearchRepository) {
        this.raceRepository = raceRepository;
        this.raceMapper = raceMapper;
        this.raceSearchRepository = raceSearchRepository;
    }

    /**
     * Save a race.
     *
     * @param raceDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceDTO save(RaceDTO raceDTO) {
        log.debug("Request to save Race : {}", raceDTO);
        Race race = raceMapper.toEntity(raceDTO);
        race = raceRepository.save(race);
        RaceDTO result = raceMapper.toDto(race);
        raceSearchRepository.save(race);
        return result;
    }

    /**
     * Get all the races.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Races");
        return raceRepository.findAll(pageable)
            .map(raceMapper::toDto);
    }


    /**
     * Get one race by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceDTO> findOne(Long id) {
        log.debug("Request to get Race : {}", id);
        return raceRepository.findById(id)
            .map(raceMapper::toDto);
    }

    /**
     * Delete the race by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Race : {}", id);
        raceRepository.deleteById(id);
        raceSearchRepository.deleteById(id);
    }

    /**
     * Search for the race corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Races for query {}", query);
        return raceSearchRepository.search(queryStringQuery(query), pageable)
            .map(raceMapper::toDto);
    }
}
