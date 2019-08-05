package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.RaceSubscriptionService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
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
 * REST controller for managing {@link it.acsi.cycling.races.domain.RaceSubscription}.
 */
@RestController
@RequestMapping("/api")
public class RaceSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(RaceSubscriptionResource.class);

    private static final String ENTITY_NAME = "raceSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaceSubscriptionService raceSubscriptionService;

    public RaceSubscriptionResource(RaceSubscriptionService raceSubscriptionService) {
        this.raceSubscriptionService = raceSubscriptionService;
    }

    /**
     * {@code POST  /race-subscriptions} : Create a new raceSubscription.
     *
     * @param raceSubscriptionDTO the raceSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raceSubscriptionDTO, or with status {@code 400 (Bad Request)} if the raceSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/race-subscriptions")
    public ResponseEntity<RaceSubscriptionDTO> createRaceSubscription(@Valid @RequestBody RaceSubscriptionDTO raceSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save RaceSubscription : {}", raceSubscriptionDTO);
        if (raceSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new raceSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaceSubscriptionDTO result = raceSubscriptionService.save(raceSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/race-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /race-subscriptions} : Updates an existing raceSubscription.
     *
     * @param raceSubscriptionDTO the raceSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raceSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the raceSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raceSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/race-subscriptions")
    public ResponseEntity<RaceSubscriptionDTO> updateRaceSubscription(@Valid @RequestBody RaceSubscriptionDTO raceSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update RaceSubscription : {}", raceSubscriptionDTO);
        if (raceSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RaceSubscriptionDTO result = raceSubscriptionService.save(raceSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raceSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /race-subscriptions} : get all the raceSubscriptions.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raceSubscriptions in body.
     */
    @GetMapping("/race-subscriptions")
    public ResponseEntity<List<RaceSubscriptionDTO>> getAllRaceSubscriptions(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of RaceSubscriptions");
        Page<RaceSubscriptionDTO> page = raceSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /race-subscriptions/:id} : get the "id" raceSubscription.
     *
     * @param id the id of the raceSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raceSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/race-subscriptions/{id}")
    public ResponseEntity<RaceSubscriptionDTO> getRaceSubscription(@PathVariable Long id) {
        log.debug("REST request to get RaceSubscription : {}", id);
        Optional<RaceSubscriptionDTO> raceSubscriptionDTO = raceSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raceSubscriptionDTO);
    }

    /**
     * {@code DELETE  /race-subscriptions/:id} : delete the "id" raceSubscription.
     *
     * @param id the id of the raceSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/race-subscriptions/{id}")
    public ResponseEntity<Void> deleteRaceSubscription(@PathVariable Long id) {
        log.debug("REST request to delete RaceSubscription : {}", id);
        raceSubscriptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/race-subscriptions?query=:query} : search for the raceSubscription corresponding
     * to the query.
     *
     * @param query the query of the raceSubscription search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/race-subscriptions")
    public ResponseEntity<List<RaceSubscriptionDTO>> searchRaceSubscriptions(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of RaceSubscriptions for query {}", query);
        Page<RaceSubscriptionDTO> page = raceSubscriptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
