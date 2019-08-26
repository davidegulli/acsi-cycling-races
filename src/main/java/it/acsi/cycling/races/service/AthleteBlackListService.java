package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.AthleteBlackList;
import it.acsi.cycling.races.repository.AthleteBlackListRepository;
import it.acsi.cycling.races.repository.search.AthleteBlackListSearchRepository;
import it.acsi.cycling.races.service.dto.AthleteBlackListDTO;
import it.acsi.cycling.races.service.mapper.AthleteBlackListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AthleteBlackListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AthleteBlackLists");
        return athleteBlackListRepository.findAll(pageable)
            .map(athleteBlackListMapper::toDto);
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AthleteBlackListDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AthleteBlackLists for query {}", query);
        return athleteBlackListSearchRepository.search(queryStringQuery(query), pageable)
            .map(athleteBlackListMapper::toDto);
    }
}
