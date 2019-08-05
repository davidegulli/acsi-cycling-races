package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.RaceSubscription;
import it.acsi.cycling.races.repository.RaceSubscriptionRepository;
import it.acsi.cycling.races.repository.search.RaceSubscriptionSearchRepository;
import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;
import it.acsi.cycling.races.service.mapper.RaceSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link RaceSubscription}.
 */
@Service
@Transactional
public class RaceSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(RaceSubscriptionService.class);

    private final RaceSubscriptionRepository raceSubscriptionRepository;

    private final RaceSubscriptionMapper raceSubscriptionMapper;

    private final RaceSubscriptionSearchRepository raceSubscriptionSearchRepository;

    public RaceSubscriptionService(RaceSubscriptionRepository raceSubscriptionRepository, RaceSubscriptionMapper raceSubscriptionMapper, RaceSubscriptionSearchRepository raceSubscriptionSearchRepository) {
        this.raceSubscriptionRepository = raceSubscriptionRepository;
        this.raceSubscriptionMapper = raceSubscriptionMapper;
        this.raceSubscriptionSearchRepository = raceSubscriptionSearchRepository;
    }

    /**
     * Save a raceSubscription.
     *
     * @param raceSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceSubscriptionDTO save(RaceSubscriptionDTO raceSubscriptionDTO) {
        log.debug("Request to save RaceSubscription : {}", raceSubscriptionDTO);
        RaceSubscription raceSubscription = raceSubscriptionMapper.toEntity(raceSubscriptionDTO);
        raceSubscription = raceSubscriptionRepository.save(raceSubscription);
        RaceSubscriptionDTO result = raceSubscriptionMapper.toDto(raceSubscription);
        raceSubscriptionSearchRepository.save(raceSubscription);
        return result;
    }

    /**
     * Get all the raceSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RaceSubscriptions");
        return raceSubscriptionRepository.findAll(pageable)
            .map(raceSubscriptionMapper::toDto);
    }


    /**
     * Get one raceSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get RaceSubscription : {}", id);
        return raceSubscriptionRepository.findById(id)
            .map(raceSubscriptionMapper::toDto);
    }

    /**
     * Delete the raceSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RaceSubscription : {}", id);
        raceSubscriptionRepository.deleteById(id);
        raceSubscriptionSearchRepository.deleteById(id);
    }

    /**
     * Search for the raceSubscription corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceSubscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RaceSubscriptions for query {}", query);
        return raceSubscriptionSearchRepository.search(queryStringQuery(query), pageable)
            .map(raceSubscriptionMapper::toDto);
    }
}
