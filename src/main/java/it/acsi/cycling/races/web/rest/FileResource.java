package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.service.FileService;
import it.acsi.cycling.races.web.rest.errors.BadRequestAlertException;
import it.acsi.cycling.races.service.dto.FileDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
 * REST controller for managing {@link it.acsi.cycling.races.domain.File}.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * {@code POST  /files} : Create a new file.
     *
     * @param fileDTO the fileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDTO, or with status {@code 400 (Bad Request)} if the file has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/files")
    public ResponseEntity<FileDTO> createFile(@Valid @RequestBody FileDTO fileDTO) throws URISyntaxException {
        log.debug("REST request to save File : {}", fileDTO);
        if (fileDTO.getId() != null) {
            throw new BadRequestAlertException("A new file cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDTO result = fileService.save(fileDTO);
        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files} : Updates an existing file.
     *
     * @param fileDTO the fileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDTO,
     * or with status {@code 400 (Bad Request)} if the fileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/files")
    public ResponseEntity<FileDTO> updateFile(@Valid @RequestBody FileDTO fileDTO) throws URISyntaxException {
        log.debug("REST request to update File : {}", fileDTO);
        if (fileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileDTO result = fileService.save(fileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /files} : get all the files.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body.
     */
    @GetMapping("/files")
    public List<FileDTO> getAllFiles() {
        log.debug("REST request to get all Files");
        return fileService.findAll();
    }

    /**
     * {@code GET  /files/:id} : get the "id" file.
     *
     * @param id the id of the fileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        Optional<FileDTO> fileDTO = fileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDTO);
    }

    /**
     * {@code GET  /files/:id} : get the "id" file.
     *
     * @param id the id of the fileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(
        value = "/files/{id}/download",
        produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBinary(@PathVariable Long id) {

        log.debug("REST request to get File : {}", id);

        Optional<FileDTO> fileDTO = fileService.findOne(id);

        if(fileDTO.isPresent()) {
            FileDTO result = fileDTO.get();
            MediaType mediaType = MediaType.parseMediaType(result.getMimeType());
            return ResponseEntity.ok().contentType(mediaType).body(fileDTO.get().getBinary());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" file.
     *
     * @param id the id of the fileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete File : {}", id);
        fileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/files?query=:query} : search for the file corresponding
     * to the query.
     *
     * @param query the query of the file search.
     * @return the result of the search.
     */
    @GetMapping("/_search/files")
    public List<FileDTO> searchFiles(@RequestParam String query) {
        log.debug("REST request to search Files for query {}", query);
        return fileService.search(query);
    }

}
