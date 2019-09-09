package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.File;
import it.acsi.cycling.races.repository.FileRepository;
import it.acsi.cycling.races.repository.search.FileSearchRepository;
import it.acsi.cycling.races.service.dto.FileDTO;
import it.acsi.cycling.races.service.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
@Transactional
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    private final FileSearchRepository fileSearchRepository;

    public FileService(FileRepository fileRepository, FileMapper fileMapper, FileSearchRepository fileSearchRepository) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileSearchRepository = fileSearchRepository;
    }

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    public FileDTO save(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        setFileUrl(file);

        file = fileRepository.save(file);
        FileDTO result = fileMapper.toDto(file);
        fileSearchRepository.save(file);
        return result;
    }

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    public FileDTO saveWithoutIndexing(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        setFileUrl(file);

        file = fileRepository.save(file);
        FileDTO result = fileMapper.toDto(file);

        return result;
    }

    public File setFileUrl(File file) {

        if (file.getUrl() == null || file.getUrl().isEmpty()) {
            String url = new StringBuilder()
                .append("/api/files/")
                .append(file.getId())
                .append("/download")
                .toString();

            file.setUrl(url);
        }

        return file;
    }

    /**
     * Get all the files.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FileDTO> findAll() {
        log.debug("Request to get all Files");
        return fileRepository.findAll().stream()
            .map(fileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one file by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id)
            .map(fileMapper::toDto);
    }

    /**
     * Delete the file by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
        fileSearchRepository.deleteById(id);
    }

    /**
     * Search for the file corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FileDTO> search(String query) {
        log.debug("Request to search Files for query {}", query);
        return StreamSupport
            .stream(fileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(fileMapper::toDto)
            .collect(Collectors.toList());
    }
}
