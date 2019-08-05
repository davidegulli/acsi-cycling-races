package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.AcsiTeamService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;

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
 * REST controller for managing {@link it.acsi.cycling.races.domain.AcsiTeam}.
 */
@RestController
@RequestMapping("/api")
public class AcsiTeamResource {

    private final Logger log = LoggerFactory.getLogger(AcsiTeamResource.class);

    private static final String ENTITY_NAME = "acsiTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcsiTeamService acsiTeamService;

    public AcsiTeamResource(AcsiTeamService acsiTeamService) {
        this.acsiTeamService = acsiTeamService;
    }

    /**
     * {@code POST  /acsi-teams} : Create a new acsiTeam.
     *
     * @param acsiTeamDTO the acsiTeamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acsiTeamDTO, or with status {@code 400 (Bad Request)} if the acsiTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acsi-teams")
    public ResponseEntity<AcsiTeamDTO> createAcsiTeam(@Valid @RequestBody AcsiTeamDTO acsiTeamDTO) throws URISyntaxException {
        log.debug("REST request to save AcsiTeam : {}", acsiTeamDTO);
        if (acsiTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new acsiTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcsiTeamDTO result = acsiTeamService.save(acsiTeamDTO);
        return ResponseEntity.created(new URI("/api/acsi-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acsi-teams} : Updates an existing acsiTeam.
     *
     * @param acsiTeamDTO the acsiTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acsiTeamDTO,
     * or with status {@code 400 (Bad Request)} if the acsiTeamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acsiTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acsi-teams")
    public ResponseEntity<AcsiTeamDTO> updateAcsiTeam(@Valid @RequestBody AcsiTeamDTO acsiTeamDTO) throws URISyntaxException {
        log.debug("REST request to update AcsiTeam : {}", acsiTeamDTO);
        if (acsiTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AcsiTeamDTO result = acsiTeamService.save(acsiTeamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, acsiTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /acsi-teams} : get all the acsiTeams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acsiTeams in body.
     */
    @GetMapping("/acsi-teams")
    public List<AcsiTeamDTO> getAllAcsiTeams() {
        log.debug("REST request to get all AcsiTeams");
        return acsiTeamService.findAll();
    }

    /**
     * {@code GET  /acsi-teams/:id} : get the "id" acsiTeam.
     *
     * @param id the id of the acsiTeamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acsiTeamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acsi-teams/{id}")
    public ResponseEntity<AcsiTeamDTO> getAcsiTeam(@PathVariable Long id) {
        log.debug("REST request to get AcsiTeam : {}", id);
        Optional<AcsiTeamDTO> acsiTeamDTO = acsiTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acsiTeamDTO);
    }

    /**
     * {@code DELETE  /acsi-teams/:id} : delete the "id" acsiTeam.
     *
     * @param id the id of the acsiTeamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acsi-teams/{id}")
    public ResponseEntity<Void> deleteAcsiTeam(@PathVariable Long id) {
        log.debug("REST request to delete AcsiTeam : {}", id);
        acsiTeamService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/acsi-teams?query=:query} : search for the acsiTeam corresponding
     * to the query.
     *
     * @param query the query of the acsiTeam search.
     * @return the result of the search.
     */
    @GetMapping("/_search/acsi-teams")
    public List<AcsiTeamDTO> searchAcsiTeams(@RequestParam String query) {
        log.debug("REST request to search AcsiTeams for query {}", query);
        return acsiTeamService.search(query);
    }

}
