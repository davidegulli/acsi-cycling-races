package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.RaceTypeService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.RaceTypeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link it.acsi.cycling.races.domain.RaceType}.
 */
@RestController
@RequestMapping("/api")
public class RaceTypeResource {

    private final Logger log = LoggerFactory.getLogger(RaceTypeResource.class);

    private static final String ENTITY_NAME = "raceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaceTypeService raceTypeService;

    public RaceTypeResource(RaceTypeService raceTypeService) {
        this.raceTypeService = raceTypeService;
    }

    /**
     * {@code POST  /race-types} : Create a new raceType.
     *
     * @param raceTypeDTO the raceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raceTypeDTO, or with status {@code 400 (Bad Request)} if the raceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/race-types")
    public ResponseEntity<RaceTypeDTO> createRaceType(@RequestBody RaceTypeDTO raceTypeDTO) throws URISyntaxException {
        log.debug("REST request to save RaceType : {}", raceTypeDTO);
        if (raceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new raceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaceTypeDTO result = raceTypeService.save(raceTypeDTO);
        return ResponseEntity.created(new URI("/api/race-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /race-types} : Updates an existing raceType.
     *
     * @param raceTypeDTO the raceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the raceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/race-types")
    public ResponseEntity<RaceTypeDTO> updateRaceType(@RequestBody RaceTypeDTO raceTypeDTO) throws URISyntaxException {
        log.debug("REST request to update RaceType : {}", raceTypeDTO);
        if (raceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RaceTypeDTO result = raceTypeService.save(raceTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raceTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /race-types} : get all the raceTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raceTypes in body.
     */
    @GetMapping("/race-types")
    public List<RaceTypeDTO> getAllRaceTypes() {
        log.debug("REST request to get all RaceTypes");
        return raceTypeService.findAll();
    }

    /**
     * {@code GET  /race-types/:id} : get the "id" raceType.
     *
     * @param id the id of the raceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/race-types/{id}")
    public ResponseEntity<RaceTypeDTO> getRaceType(@PathVariable Long id) {
        log.debug("REST request to get RaceType : {}", id);
        Optional<RaceTypeDTO> raceTypeDTO = raceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raceTypeDTO);
    }

    /**
     * {@code DELETE  /race-types/:id} : delete the "id" raceType.
     *
     * @param id the id of the raceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/race-types/{id}")
    public ResponseEntity<Void> deleteRaceType(@PathVariable Long id) {
        log.debug("REST request to delete RaceType : {}", id);
        raceTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/race-types?query=:query} : search for the raceType corresponding
     * to the query.
     *
     * @param query the query of the raceType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/race-types")
    public List<RaceTypeDTO> searchRaceTypes(@RequestParam String query) {
        log.debug("REST request to search RaceTypes for query {}", query);
        return raceTypeService.search(query);
    }

}
