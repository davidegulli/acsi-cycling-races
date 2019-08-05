package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.NonAcsiTeamService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.NonAcsiTeamDTO;

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
 * REST controller for managing {@link it.acsi.cycling.races.domain.NonAcsiTeam}.
 */
@RestController
@RequestMapping("/api")
public class NonAcsiTeamResource {

    private final Logger log = LoggerFactory.getLogger(NonAcsiTeamResource.class);

    private static final String ENTITY_NAME = "nonAcsiTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NonAcsiTeamService nonAcsiTeamService;

    public NonAcsiTeamResource(NonAcsiTeamService nonAcsiTeamService) {
        this.nonAcsiTeamService = nonAcsiTeamService;
    }

    /**
     * {@code POST  /non-acsi-teams} : Create a new nonAcsiTeam.
     *
     * @param nonAcsiTeamDTO the nonAcsiTeamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nonAcsiTeamDTO, or with status {@code 400 (Bad Request)} if the nonAcsiTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/non-acsi-teams")
    public ResponseEntity<NonAcsiTeamDTO> createNonAcsiTeam(@Valid @RequestBody NonAcsiTeamDTO nonAcsiTeamDTO) throws URISyntaxException {
        log.debug("REST request to save NonAcsiTeam : {}", nonAcsiTeamDTO);
        if (nonAcsiTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new nonAcsiTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NonAcsiTeamDTO result = nonAcsiTeamService.save(nonAcsiTeamDTO);
        return ResponseEntity.created(new URI("/api/non-acsi-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /non-acsi-teams} : Updates an existing nonAcsiTeam.
     *
     * @param nonAcsiTeamDTO the nonAcsiTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nonAcsiTeamDTO,
     * or with status {@code 400 (Bad Request)} if the nonAcsiTeamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nonAcsiTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/non-acsi-teams")
    public ResponseEntity<NonAcsiTeamDTO> updateNonAcsiTeam(@Valid @RequestBody NonAcsiTeamDTO nonAcsiTeamDTO) throws URISyntaxException {
        log.debug("REST request to update NonAcsiTeam : {}", nonAcsiTeamDTO);
        if (nonAcsiTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NonAcsiTeamDTO result = nonAcsiTeamService.save(nonAcsiTeamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nonAcsiTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /non-acsi-teams} : get all the nonAcsiTeams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nonAcsiTeams in body.
     */
    @GetMapping("/non-acsi-teams")
    public List<NonAcsiTeamDTO> getAllNonAcsiTeams() {
        log.debug("REST request to get all NonAcsiTeams");
        return nonAcsiTeamService.findAll();
    }

    /**
     * {@code GET  /non-acsi-teams/:id} : get the "id" nonAcsiTeam.
     *
     * @param id the id of the nonAcsiTeamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nonAcsiTeamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/non-acsi-teams/{id}")
    public ResponseEntity<NonAcsiTeamDTO> getNonAcsiTeam(@PathVariable Long id) {
        log.debug("REST request to get NonAcsiTeam : {}", id);
        Optional<NonAcsiTeamDTO> nonAcsiTeamDTO = nonAcsiTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nonAcsiTeamDTO);
    }

    /**
     * {@code DELETE  /non-acsi-teams/:id} : delete the "id" nonAcsiTeam.
     *
     * @param id the id of the nonAcsiTeamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/non-acsi-teams/{id}")
    public ResponseEntity<Void> deleteNonAcsiTeam(@PathVariable Long id) {
        log.debug("REST request to delete NonAcsiTeam : {}", id);
        nonAcsiTeamService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/non-acsi-teams?query=:query} : search for the nonAcsiTeam corresponding
     * to the query.
     *
     * @param query the query of the nonAcsiTeam search.
     * @return the result of the search.
     */
    @GetMapping("/_search/non-acsi-teams")
    public List<NonAcsiTeamDTO> searchNonAcsiTeams(@RequestParam String query) {
        log.debug("REST request to search NonAcsiTeams for query {}", query);
        return nonAcsiTeamService.search(query);
    }

}
