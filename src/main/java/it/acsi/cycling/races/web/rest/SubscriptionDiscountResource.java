package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.SubscriptionDiscountService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.SubscriptionDiscountDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link it.acsi.cycling.races.domain.SubscriptionDiscount}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionDiscountResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionDiscountResource.class);

    private static final String ENTITY_NAME = "subscriptionDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionDiscountService subscriptionDiscountService;

    public SubscriptionDiscountResource(SubscriptionDiscountService subscriptionDiscountService) {
        this.subscriptionDiscountService = subscriptionDiscountService;
    }

    /**
     * {@code POST  /subscription-discounts} : Create a new subscriptionDiscount.
     *
     * @param subscriptionDiscountDTO the subscriptionDiscountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionDiscountDTO, or with status {@code 400 (Bad Request)} if the subscriptionDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-discounts")
    public ResponseEntity<SubscriptionDiscountDTO> createSubscriptionDiscount(@Valid @RequestBody SubscriptionDiscountDTO subscriptionDiscountDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionDiscount : {}", subscriptionDiscountDTO);
        if (subscriptionDiscountDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionDiscountDTO result = subscriptionDiscountService.save(subscriptionDiscountDTO);
        return ResponseEntity.created(new URI("/api/subscription-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-discounts} : Updates an existing subscriptionDiscount.
     *
     * @param subscriptionDiscountDTO the subscriptionDiscountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDiscountDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionDiscountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDiscountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-discounts")
    public ResponseEntity<SubscriptionDiscountDTO> updateSubscriptionDiscount(@Valid @RequestBody SubscriptionDiscountDTO subscriptionDiscountDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionDiscount : {}", subscriptionDiscountDTO);
        if (subscriptionDiscountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionDiscountDTO result = subscriptionDiscountService.save(subscriptionDiscountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionDiscountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-discounts} : get all the subscriptionDiscounts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionDiscounts in body.
     */
    @GetMapping("/subscription-discounts")
    public List<SubscriptionDiscountDTO> getAllSubscriptionDiscounts() {
        log.debug("REST request to get all SubscriptionDiscounts");
        return subscriptionDiscountService.findAll();
    }

    /**
     * {@code GET  /subscription-discounts/:id} : get the "id" subscriptionDiscount.
     *
     * @param id the id of the subscriptionDiscountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionDiscountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-discounts/{id}")
    public ResponseEntity<SubscriptionDiscountDTO> getSubscriptionDiscount(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionDiscount : {}", id);
        Optional<SubscriptionDiscountDTO> subscriptionDiscountDTO = subscriptionDiscountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionDiscountDTO);
    }

    /**
     * {@code DELETE  /subscription-discounts/:id} : delete the "id" subscriptionDiscount.
     *
     * @param id the id of the subscriptionDiscountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-discounts/{id}")
    public ResponseEntity<Void> deleteSubscriptionDiscount(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionDiscount : {}", id);
        subscriptionDiscountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/subscription-discounts?query=:query} : search for the subscriptionDiscount corresponding
     * to the query.
     *
     * @param query the query of the subscriptionDiscount search.
     * @return the result of the search.
     */
    @GetMapping("/_search/subscription-discounts")
    public List<SubscriptionDiscountDTO> searchSubscriptionDiscounts(@RequestParam String query) {
        log.debug("REST request to search SubscriptionDiscounts for query {}", query);
        return subscriptionDiscountService.search(query);
    }

}
