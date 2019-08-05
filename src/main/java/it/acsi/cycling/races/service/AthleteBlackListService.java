package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.AthleteBlackList;
import it.acsi.cycling.races.repository.AthleteBlackListRepository;
import it.acsi.cycling.races.repository.search.AthleteBlackListSearchRepository;
import it.acsi.cycling.races.service.dto.AthleteBlackListDTO;
import it.acsi.cycling.races.service.mapper.AthleteBlackListMapper;
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
 * Service Implementation for managing {@link AthleteBlackList}.
 */
@Service
@Transactional
public class AthleteBlackListService {

    private final Logger log = LoggerFactory.getLogger(AthleteBlackListService.class);

    private final AthleteBlackListRepository athleteBlackListRepository;

    private final AthleteBlackListMapper athleteBlackListMapper;

    private final AthleteBlackListSearchRepository athleteBlackListSearchRepository;

    public AthleteBlackListService(AthleteBlackListRepository athleteBlackListRepository, AthleteBlackListMapper athleteBlackListMapper, AthleteBlackListSearchRepository athleteBlackListSearchRepository) {
        this.athleteBlackListRepository = athleteBlackListRepository;
        this.athleteBlackListMapper = athleteBlackListMapper;
        this.athleteBlackListSearchRepository = athleteBlackListSearchRepository;
    }

    /**
     * Save a athleteBlackList.
     *
     * @param athleteBlackListDTO the entity to save.
     * @return the persisted entity.
     */
    public AthleteBlackListDTO save(AthleteBlackListDTO athleteBlackListDTO) {
        log.debug("Request to save AthleteBlackList : {}", athleteBlackListDTO);
        AthleteBlackList athleteBlackList = athleteBlackListMapper.toEntity(athleteBlackListDTO);
        athleteBlackList = athleteBlackListRepository.save(athleteBlackList);
        AthleteBlackListDTO result = athleteBlackListMapper.toDto(athleteBlackList);
        athleteBlackListSearchRepository.save(athleteBlackList);
        return result;
    }

    /**
     * Get all the athleteBlackLists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AthleteBlackListDTO> findAll() {
        log.debug("Request to get all AthleteBlackLists");
        return athleteBlackListRepository.findAll().stream()
            .map(athleteBlackListMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one athleteBlackList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AthleteBlackListDTO> findOne(Long id) {
        log.debug("Request to get AthleteBlackList : {}", id);
        return athleteBlackListRepository.findById(id)
            .map(athleteBlackListMapper::toDto);
    }

    /**
     * Delete the athleteBlackList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AthleteBlackList : {}", id);
        athleteBlackListRepository.deleteById(id);
        athleteBlackListSearchRepository.deleteById(id);
    }

    /**
     * Search for the athleteBlackList corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AthleteBlackListDTO> search(String query) {
        log.debug("Request to search AthleteBlackLists for query {}", query);
        return StreamSupport
            .stream(athleteBlackListSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(athleteBlackListMapper::toDto)
            .collect(Collectors.toList());
    }
}
