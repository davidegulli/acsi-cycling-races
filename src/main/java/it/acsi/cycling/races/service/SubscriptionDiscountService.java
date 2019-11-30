package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.SubscriptionDiscount;
import it.acsi.cycling.races.repository.SubscriptionDiscountRepository;
import it.acsi.cycling.races.repository.search.SubscriptionDiscountSearchRepository;
import it.acsi.cycling.races.service.dto.SubscriptionDiscountDTO;
import it.acsi.cycling.races.service.mapper.SubscriptionDiscountMapper;
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
 * Service Implementation for managing {@link SubscriptionDiscount}.
 */
@Service
@Transactional
public class SubscriptionDiscountService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionDiscountService.class);

    private final SubscriptionDiscountRepository subscriptionDiscountRepository;

    private final SubscriptionDiscountMapper subscriptionDiscountMapper;

    private final SubscriptionDiscountSearchRepository subscriptionDiscountSearchRepository;

    public SubscriptionDiscountService(SubscriptionDiscountRepository subscriptionDiscountRepository, SubscriptionDiscountMapper subscriptionDiscountMapper, SubscriptionDiscountSearchRepository subscriptionDiscountSearchRepository) {
        this.subscriptionDiscountRepository = subscriptionDiscountRepository;
        this.subscriptionDiscountMapper = subscriptionDiscountMapper;
        this.subscriptionDiscountSearchRepository = subscriptionDiscountSearchRepository;
    }

    /**
     * Save a subscriptionDiscount.
     *
     * @param subscriptionDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDiscountDTO save(SubscriptionDiscountDTO subscriptionDiscountDTO) {
        log.debug("Request to save SubscriptionDiscount : {}", subscriptionDiscountDTO);
        SubscriptionDiscount subscriptionDiscount = subscriptionDiscountMapper.toEntity(subscriptionDiscountDTO);
        subscriptionDiscount = subscriptionDiscountRepository.save(subscriptionDiscount);
        SubscriptionDiscountDTO result = subscriptionDiscountMapper.toDto(subscriptionDiscount);
        subscriptionDiscountSearchRepository.save(subscriptionDiscount);
        return result;
    }

    /**
     * Get all the subscriptionDiscounts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDiscountDTO> findAll() {
        log.debug("Request to get all SubscriptionDiscounts");
        return subscriptionDiscountRepository.findAll().stream()
            .map(subscriptionDiscountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one subscriptionDiscount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionDiscountDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionDiscount : {}", id);
        return subscriptionDiscountRepository.findById(id)
            .map(subscriptionDiscountMapper::toDto);
    }

    /**
     * Delete the subscriptionDiscount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionDiscount : {}", id);
        subscriptionDiscountRepository.deleteById(id);
        subscriptionDiscountSearchRepository.deleteById(id);
    }

    /**
     * Search for the subscriptionDiscount corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDiscountDTO> search(String query) {
        log.debug("Request to search SubscriptionDiscounts for query {}", query);
        return StreamSupport
            .stream(subscriptionDiscountSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(subscriptionDiscountMapper::toDto)
            .collect(Collectors.toList());
    }
}
