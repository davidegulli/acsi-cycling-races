package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.SubscriptionType;
import it.acsi.cycling.races.repository.SubscriptionTypeRepository;
import it.acsi.cycling.races.repository.search.SubscriptionTypeSearchRepository;
import it.acsi.cycling.races.service.dto.SubscriptionTypeDTO;
import it.acsi.cycling.races.service.mapper.SubscriptionTypeMapper;
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
 * Service Implementation for managing {@link SubscriptionType}.
 */
@Service
@Transactional
public class SubscriptionTypeService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionTypeService.class);

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    private final SubscriptionTypeMapper subscriptionTypeMapper;

    private final SubscriptionTypeSearchRepository subscriptionTypeSearchRepository;

    public SubscriptionTypeService(SubscriptionTypeRepository subscriptionTypeRepository, SubscriptionTypeMapper subscriptionTypeMapper, SubscriptionTypeSearchRepository subscriptionTypeSearchRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
        this.subscriptionTypeMapper = subscriptionTypeMapper;
        this.subscriptionTypeSearchRepository = subscriptionTypeSearchRepository;
    }

    /**
     * Save a subscriptionType.
     *
     * @param subscriptionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionTypeDTO save(SubscriptionTypeDTO subscriptionTypeDTO) {
        log.debug("Request to save SubscriptionType : {}", subscriptionTypeDTO);
        SubscriptionType subscriptionType = subscriptionTypeMapper.toEntity(subscriptionTypeDTO);
        subscriptionType = subscriptionTypeRepository.save(subscriptionType);
        SubscriptionTypeDTO result = subscriptionTypeMapper.toDto(subscriptionType);
        subscriptionTypeSearchRepository.save(subscriptionType);
        return result;
    }

    /**
     * Get all the subscriptionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionTypeDTO> findAll() {
        log.debug("Request to get all SubscriptionTypes");
        return subscriptionTypeRepository.findAll().stream()
            .map(subscriptionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one subscriptionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionTypeDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionType : {}", id);
        return subscriptionTypeRepository.findById(id)
            .map(subscriptionTypeMapper::toDto);
    }

    /**
     * Get all the subscriptionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionTypeDTO> findByRaceId(Long raceId) {
        log.debug("Request to get all SubscriptionTypes");
        return subscriptionTypeRepository.findByRaceId(raceId).stream()
            .map(subscriptionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the subscriptionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionType : {}", id);
        subscriptionTypeRepository.deleteById(id);
        subscriptionTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the subscriptionType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionTypeDTO> search(String query) {
        log.debug("Request to search SubscriptionTypes for query {}", query);
        return StreamSupport
            .stream(subscriptionTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(subscriptionTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
