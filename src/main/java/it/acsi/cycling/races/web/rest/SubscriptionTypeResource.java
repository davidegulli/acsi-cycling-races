package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.SubscriptionTypeService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.SubscriptionTypeDTO;

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
 * REST controller for managing {@link it.acsi.cycling.races.domain.SubscriptionType}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionTypeResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionTypeResource.class);

    private static final String ENTITY_NAME = "subscriptionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionTypeService subscriptionTypeService;

    public SubscriptionTypeResource(SubscriptionTypeService subscriptionTypeService) {
        this.subscriptionTypeService = subscriptionTypeService;
    }

    /**
     * {@code POST  /subscription-types} : Create a new subscriptionType.
     *
     * @param subscriptionTypeDTO the subscriptionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionTypeDTO, or with status {@code 400 (Bad Request)} if the subscriptionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-types")
    public ResponseEntity<SubscriptionTypeDTO> createSubscriptionType(@Valid @RequestBody SubscriptionTypeDTO subscriptionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionType : {}", subscriptionTypeDTO);
        if (subscriptionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionTypeDTO result = subscriptionTypeService.save(subscriptionTypeDTO);
        return ResponseEntity.created(new URI("/api/subscription-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-types} : Updates an existing subscriptionType.
     *
     * @param subscriptionTypeDTO the subscriptionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-types")
    public ResponseEntity<SubscriptionTypeDTO> updateSubscriptionType(@Valid @RequestBody SubscriptionTypeDTO subscriptionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionType : {}", subscriptionTypeDTO);
        if (subscriptionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionTypeDTO result = subscriptionTypeService.save(subscriptionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-types} : get all the subscriptionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionTypes in body.
     */
    @GetMapping("/subscription-types")
    public List<SubscriptionTypeDTO> getAllSubscriptionTypes() {
        log.debug("REST request to get all SubscriptionTypes");
        return subscriptionTypeService.findAll();
    }

    /**
     * {@code GET  /subscription-types/:id} : get the "id" subscriptionType.
     *
     * @param id the id of the subscriptionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-types/{id}")
    public ResponseEntity<SubscriptionTypeDTO> getSubscriptionType(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionType : {}", id);
        Optional<SubscriptionTypeDTO> subscriptionTypeDTO = subscriptionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionTypeDTO);
    }

    /**
     * {@code DELETE  /subscription-types/:id} : delete the "id" subscriptionType.
     *
     * @param id the id of the subscriptionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-types/{id}")
    public ResponseEntity<Void> deleteSubscriptionType(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionType : {}", id);
        subscriptionTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/subscription-types?query=:query} : search for the subscriptionType corresponding
     * to the query.
     *
     * @param query the query of the subscriptionType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/subscription-types")
    public List<SubscriptionTypeDTO> searchSubscriptionTypes(@RequestParam String query) {
        log.debug("REST request to search SubscriptionTypes for query {}", query);
        return subscriptionTypeService.search(query);
    }

}
