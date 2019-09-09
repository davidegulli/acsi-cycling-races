package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.Contact;
import it.acsi.cycling.races.domain.File;
import it.acsi.cycling.races.domain.Race;
import it.acsi.cycling.races.domain.enumeration.FileType;
import it.acsi.cycling.races.repository.ContactRepository;
import it.acsi.cycling.races.repository.FileRepository;
import it.acsi.cycling.races.repository.RaceRepository;
import it.acsi.cycling.races.repository.search.RaceSearchRepository;
import it.acsi.cycling.races.service.dto.RaceDTO;
import it.acsi.cycling.races.service.mapper.RaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Race}.
 */
@Service
@Transactional
public class RaceService {

    private final Logger log = LoggerFactory.getLogger(RaceService.class);

    private final RaceRepository raceRepository;

    private final FileRepository fileRepository;

    private final ContactRepository contactRepository;

    private final RaceMapper raceMapper;

    private final RaceSearchRepository raceSearchRepository;

    private final FileService fileService;

    public RaceService(
            RaceRepository raceRepository, RaceMapper raceMapper, RaceSearchRepository raceSearchRepository,
            FileRepository fileRepository, ContactRepository contactRepository, FileService fileService) {
        this.raceRepository = raceRepository;
        this.fileRepository = fileRepository;
        this.contactRepository = contactRepository;
        this.raceMapper = raceMapper;
        this.raceSearchRepository = raceSearchRepository;
        this.fileService = fileService;
    }

    /**
     * Save a race.
     *
     * @param raceDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceDTO save(RaceDTO raceDTO) {

        log.debug("Request to save Race : {}", raceDTO);

        Race race = raceMapper.toEntity(raceDTO);
        race = raceRepository.save(race);
        RaceDTO result = raceMapper.toDto(race);

        File logoImage = new File()
            .mimeType(raceDTO.getBinaryLogoContentType())
            .title(raceDTO.getBinaryLogoFileName())
            .binary(raceDTO.getBinaryLogoImage())
            .binaryContentType(raceDTO.getBinaryLogoContentType())
            .type(FileType.LOGO_IMAGE)
            .race(race);
        logoImage = fileRepository.save(logoImage);
        fileService.setFileUrl(logoImage);
        fileRepository.save(logoImage);

        File coverImage = new File()
            .mimeType(raceDTO.getBinaryCoverContentType())
            .title(raceDTO.getBinaryCoverFileName())
            .binary(raceDTO.getBinaryCoverImage())
            .binaryContentType(raceDTO.getBinaryCoverContentType())
            .type(FileType.COVER_IMAGE)
            .race(race);
        coverImage = fileRepository.save(coverImage);
        fileService.setFileUrl(coverImage);
        fileRepository.save(coverImage);

        File pathMapImage = new File()
            .mimeType(raceDTO.getBinaryPathMapContentType())
            .title(raceDTO.getBinaryPathMapFileName())
            .binary(raceDTO.getBinaryPathMapImage())
            .binaryContentType(raceDTO.getBinaryPathMapContentType())
            .type(FileType.PATH_IMAGE)
            .race(race);
        pathMapImage = fileRepository.save(pathMapImage);
        fileService.setFileUrl(pathMapImage);
        fileRepository.save(pathMapImage);

        Contact contact = new Contact()
            .name(raceDTO.getContactName())
            .email(raceDTO.getContactEmail())
            .phone(raceDTO.getContactPhone())
            .race(race);
        contactRepository.save(contact);

        raceSearchRepository.save(race);

        return result;
    }

    /**
     * Get all the races.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceDTO> findAll(Pageable pageable) {

        log.debug("Request to get all Races");

        return raceRepository.findAll(pageable)
            .map(raceMapper::toDtoWithChildRelation);
    }

    /**
     * Get one race by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceDTO> findOne(Long id) {

        log.debug("Request to get Race : {}", id);

        return raceRepository.findById(id)
            .map(raceMapper::toDtoWithChildRelation);
/*
        if(optRace.isPresent()) {
            Race race = optRace.get();
            RaceDTO result = raceMapper.toDto(race);

            race.getContacts()
                .stream()
                .findFirst()
                .ifPresent(c -> {
                    result.setContactName(c.getName());
                    result.setContactEmail(c.getEmail());
                    result.setContactPhone(c.getPhone());
                });

            race.getAttachments()
                .stream()
                .filter(f -> f.getType().equals(FileType.LOGO_IMAGE))
                .forEach(f -> {
                    result.setBinaryLogoUrl(f.getUrl());
                });

            race.getAttachments()
                .stream()
                .filter(f -> f.getType().equals(FileType.COVER_IMAGE))
                .forEach(f -> {
                    result.setBinaryCoverUrl(f.getUrl());
                });

            race.getAttachments()
                .stream()
                .filter(f -> f.getType().equals(FileType.PATH_IMAGE))
                .forEach(f -> {
                    result.setBinaryPathMapUrl(f.getUrl());
                });

            return Optional.of(result);
        }

        return Optional.empty();

 */
    }

    /**
     * Delete the race by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Race : {}", id);
        raceRepository.deleteById(id);
        raceSearchRepository.deleteById(id);
    }

    /**
     * Search for the race corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Races for query {}", query);
        return raceSearchRepository.search(queryStringQuery(query), pageable)
            .map(raceMapper::toDto);
    }
}
