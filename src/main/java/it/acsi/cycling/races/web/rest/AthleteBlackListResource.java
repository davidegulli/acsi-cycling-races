package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.AthleteBlackListService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.AthleteBlackListDTO;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
 * REST controller for managing {@link it.acsi.cycling.races.domain.AthleteBlackList}.
 */
@RestController
@RequestMapping("/api")
public class AthleteBlackListResource {

    private final Logger log = LoggerFactory.getLogger(AthleteBlackListResource.class);

    private static final String ENTITY_NAME = "athleteBlackList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AthleteBlackListService athleteBlackListService;

    public AthleteBlackListResource(AthleteBlackListService athleteBlackListService) {
        this.athleteBlackListService = athleteBlackListService;
    }

    /**
     * {@code POST  /athlete-black-lists} : Create a new athleteBlackList.
     *
     * @param athleteBlackListDTO the athleteBlackListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new athleteBlackListDTO, or with status {@code 400 (Bad Request)} if the athleteBlackList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/athlete-black-lists")
    public ResponseEntity<AthleteBlackListDTO> createAthleteBlackList(@Valid @RequestBody AthleteBlackListDTO athleteBlackListDTO) throws URISyntaxException {
        log.debug("REST request to save AthleteBlackList : {}", athleteBlackListDTO);
        if (athleteBlackListDTO.getId() != null) {
            throw new BadRequestAlertException("A new athleteBlackList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AthleteBlackListDTO result = athleteBlackListService.save(athleteBlackListDTO);
        return ResponseEntity.created(new URI("/api/athlete-black-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /athlete-black-lists} : Updates an existing athleteBlackList.
     *
     * @param athleteBlackListDTO the athleteBlackListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated athleteBlackListDTO,
     * or with status {@code 400 (Bad Request)} if the athleteBlackListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the athleteBlackListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/athlete-black-lists")
    public ResponseEntity<AthleteBlackListDTO> updateAthleteBlackList(@Valid @RequestBody AthleteBlackListDTO athleteBlackListDTO) throws URISyntaxException {
        log.debug("REST request to update AthleteBlackList : {}", athleteBlackListDTO);
        if (athleteBlackListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AthleteBlackListDTO result = athleteBlackListService.save(athleteBlackListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, athleteBlackListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /athlete-black-lists} : get all the athleteBlackLists.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of athleteBlackLists in body.
     */
    @GetMapping("/athlete-black-lists")
    public ResponseEntity<List<AthleteBlackListDTO>> getAllAthleteBlackLists(Pageable pageable) {
        log.debug("REST request to get a page of AthleteBlackLists");
        Page<AthleteBlackListDTO> page = athleteBlackListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /athlete-black-lists/:id} : get the "id" athleteBlackList.
     *
     * @param id the id of the athleteBlackListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the athleteBlackListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/athlete-black-lists/{id}")
    public ResponseEntity<AthleteBlackListDTO> getAthleteBlackList(@PathVariable Long id) {
        log.debug("REST request to get AthleteBlackList : {}", id);
        Optional<AthleteBlackListDTO> athleteBlackListDTO = athleteBlackListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(athleteBlackListDTO);
    }

    /**
     * {@code DELETE  /athlete-black-lists/:id} : delete the "id" athleteBlackList.
     *
     * @param id the id of the athleteBlackListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/athlete-black-lists/{id}")
    public ResponseEntity<Void> deleteAthleteBlackList(@PathVariable Long id) {
        log.debug("REST request to delete AthleteBlackList : {}", id);
        athleteBlackListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/athlete-black-lists?query=:query} : search for the athleteBlackList corresponding
     * to the query.
     *
     * @param query the query of the athleteBlackList search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/athlete-black-lists")
    public ResponseEntity<List<AthleteBlackListDTO>> searchAthleteBlackLists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AthleteBlackLists for query {}", query);
        Page<AthleteBlackListDTO> page = athleteBlackListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
