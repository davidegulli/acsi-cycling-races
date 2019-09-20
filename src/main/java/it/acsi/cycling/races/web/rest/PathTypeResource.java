package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.PathTypeService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.PathTypeDTO;

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
 * REST controller for managing {@link it.acsi.cycling.races.domain.PathType}.
 */
@RestController
@RequestMapping("/api")
public class PathTypeResource {

    private final Logger log = LoggerFactory.getLogger(PathTypeResource.class);

    private static final String ENTITY_NAME = "pathType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PathTypeService pathTypeService;

    public PathTypeResource(PathTypeService pathTypeService) {
        this.pathTypeService = pathTypeService;
    }

    /**
     * {@code POST  /path-types} : Create a new pathType.
     *
     * @param pathTypeDTO the pathTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pathTypeDTO, or with status {@code 400 (Bad Request)} if the pathType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/path-types")
    public ResponseEntity<PathTypeDTO> createPathType(@Valid @RequestBody PathTypeDTO pathTypeDTO) throws URISyntaxException {
        log.debug("REST request to save PathType : {}", pathTypeDTO);
        if (pathTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pathType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PathTypeDTO result = pathTypeService.save(pathTypeDTO);
        return ResponseEntity.created(new URI("/api/path-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /path-types} : Updates an existing pathType.
     *
     * @param pathTypeDTO the pathTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pathTypeDTO,
     * or with status {@code 400 (Bad Request)} if the pathTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pathTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/path-types")
    public ResponseEntity<PathTypeDTO> updatePathType(@Valid @RequestBody PathTypeDTO pathTypeDTO) throws URISyntaxException {
        log.debug("REST request to update PathType : {}", pathTypeDTO);
        if (pathTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PathTypeDTO result = pathTypeService.save(pathTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pathTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /path-types} : get all the pathTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pathTypes in body.
     */
    @GetMapping("/path-types")
    public List<PathTypeDTO> getAllPathTypes() {
        log.debug("REST request to get all PathTypes");
        return pathTypeService.findAll();
    }

    /**
     * {@code GET  /path-types/:id} : get the "id" pathType.
     *
     * @param id the id of the pathTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pathTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/path-types/{id}")
    public ResponseEntity<PathTypeDTO> getPathType(@PathVariable Long id) {
        log.debug("REST request to get PathType : {}", id);
        Optional<PathTypeDTO> pathTypeDTO = pathTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pathTypeDTO);
    }

    /**
     * {@code GET  /path-types} : get all the pathTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pathTypes in body.
     */
    @GetMapping("/path-types/race/{raceId}")
    public List<PathTypeDTO> getPathTypesByRaceId(@PathVariable Long raceId) {
        log.debug("REST request to get PathTypes by raceId");
        return pathTypeService.findByRaceId(raceId);
    }

    /**
     * {@code DELETE  /path-types/:id} : delete the "id" pathType.
     *
     * @param id the id of the pathTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/path-types/{id}")
    public ResponseEntity<Void> deletePathType(@PathVariable Long id) {
        log.debug("REST request to delete PathType : {}", id);
        pathTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/path-types?query=:query} : search for the pathType corresponding
     * to the query.
     *
     * @param query the query of the pathType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/path-types")
    public List<PathTypeDTO> searchPathTypes(@RequestParam String query) {
        log.debug("REST request to search PathTypes for query {}", query);
        return pathTypeService.search(query);
    }

}
