package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.RaceService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.RaceDTO;

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
 * REST controller for managing {@link it.acsi.cycling.races.domain.Race}.
 */
@RestController
@RequestMapping("/api")
public class RaceResource {

    private final Logger log = LoggerFactory.getLogger(RaceResource.class);

    private static final String ENTITY_NAME = "race";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaceService raceService;

    public RaceResource(RaceService raceService) {
        this.raceService = raceService;
    }

    /**
     * {@code POST  /races} : Create a new race.
     *
     * @param raceDTO the raceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raceDTO, or with status {@code 400 (Bad Request)} if the race has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/races")
    public ResponseEntity<RaceDTO> createRace(@Valid @RequestBody RaceDTO raceDTO) throws URISyntaxException {
        log.debug("REST request to save Race : {}", raceDTO);
        if (raceDTO.getId() != null) {
            throw new BadRequestAlertException("A new race cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaceDTO result = raceService.save(raceDTO);
        return ResponseEntity.created(new URI("/api/races/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /races} : Updates an existing race.
     *
     * @param raceDTO the raceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raceDTO,
     * or with status {@code 400 (Bad Request)} if the raceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/races")
    public ResponseEntity<RaceDTO> updateRace(@Valid @RequestBody RaceDTO raceDTO) throws URISyntaxException {
        log.debug("REST request to update Race : {}", raceDTO);
        if (raceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RaceDTO result = raceService.save(raceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /races} : get all the races.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of races in body.
     */
    @GetMapping("/races")
    public ResponseEntity<List<RaceDTO>> getAllRaces(Pageable pageable) {
        log.debug("REST request to get a page of Races");
        Page<RaceDTO> page = raceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /races/:id} : get the "id" race.
     *
     * @param id the id of the raceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/races/{id}")
    public ResponseEntity<RaceDTO> getRace(@PathVariable Long id) {
        log.debug("REST request to get Race : {}", id);
        Optional<RaceDTO> raceDTO = raceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raceDTO);
    }

    /**
     * {@code DELETE  /races/:id} : delete the "id" race.
     *
     * @param id the id of the raceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/races/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        log.debug("REST request to delete Race : {}", id);
        raceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/races?query=:query} : search for the race corresponding
     * to the query.
     *
     * @param query the query of the race search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/races")
    public ResponseEntity<List<RaceDTO>> searchRaces(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Races for query {}", query);
        Page<RaceDTO> page = raceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
