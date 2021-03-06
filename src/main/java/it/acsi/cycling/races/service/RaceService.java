package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.Contact;
import it.acsi.cycling.races.domain.File;
import it.acsi.cycling.races.domain.Race;
import it.acsi.cycling.races.domain.enumeration.EntityType;
import it.acsi.cycling.races.domain.enumeration.FileType;
import it.acsi.cycling.races.repository.*;
import it.acsi.cycling.races.repository.search.RaceSearchRepository;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;
import it.acsi.cycling.races.service.dto.RaceDTO;
import it.acsi.cycling.races.service.mapper.RaceMapper;
import it.acsi.cycling.races.service.mapper.SubscriptionDiscountMapper;
import it.acsi.cycling.races.service.mapper.SubscriptionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Race}.
 */
@Service
@Transactional
public class RaceService {

    private final Logger log = LoggerFactory.getLogger(RaceService.class);

    private final RaceMapper raceMapper;

    private final SubscriptionTypeMapper subscriptionTypeMapper;

    private final SubscriptionDiscountMapper subscriptionDiscountMapper;

    private final RaceRepository raceRepository;

    private final FileRepository fileRepository;

    private final ContactRepository contactRepository;

    private final RaceSearchRepository raceSearchRepository;

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    private final SubscriptionDiscountRepository subscriptionDiscountRepository;

    private final FileService fileService;

    private final AcsiTeamService acsiTeamService;

    public RaceService(
        RaceMapper raceMapper,
        SubscriptionTypeMapper subscriptionTypeMapper,
        SubscriptionDiscountMapper subscriptionDiscountMapper,
        RaceRepository raceRepository,
        RaceSearchRepository raceSearchRepository,
        FileRepository fileRepository,
        ContactRepository contactRepository, FileService fileService,
        SubscriptionDiscountRepository subscriptionDiscountRepository,
        SubscriptionTypeRepository subscriptionTypeRepository,
        AcsiTeamService acsiTeamService) {

        this.raceMapper = raceMapper;
        this.subscriptionTypeMapper = subscriptionTypeMapper;
        this.subscriptionDiscountMapper = subscriptionDiscountMapper;
        this.raceRepository = raceRepository;
        this.fileRepository = fileRepository;
        this.contactRepository = contactRepository;
        this.raceSearchRepository = raceSearchRepository;
        this.fileService = fileService;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
        this.subscriptionDiscountRepository = subscriptionDiscountRepository;
        this.acsiTeamService = acsiTeamService;
    }

    /**
     * Save a race.
     *
     * @param raceDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceDTO save(RaceDTO raceDTO) {

        log.debug("Request to save Race : {}", raceDTO);

        boolean isNew = (raceDTO.getId() == null || raceDTO.getId() <= 0);

        Race race = raceMapper.toEntity(raceDTO);
        race = raceRepository.save(race);
        RaceDTO result = raceMapper.toDto(race);

        if(isLogoImagePresent(raceDTO)) {
            File logoImage = new File()
                .mimeType(raceDTO.getBinaryLogoContentType())
                .title(raceDTO.getBinaryLogoFileName())
                .binary(raceDTO.getBinaryLogoImage())
                .binaryContentType(raceDTO.getBinaryLogoContentType())
                .type(FileType.LOGO_IMAGE)
                .entityType(EntityType.RACE)
                .entityId(race.getId());
            logoImage = fileRepository.save(logoImage);
            fileService.setFileUrl(logoImage);
            fileRepository.save(logoImage);
        }

        if(isCoverImagePresent(raceDTO)) {
            File coverImage = new File()
                .mimeType(raceDTO.getBinaryCoverContentType())
                .title(raceDTO.getBinaryCoverFileName())
                .binary(raceDTO.getBinaryCoverImage())
                .binaryContentType(raceDTO.getBinaryCoverContentType())
                .type(FileType.COVER_IMAGE)
                .entityType(EntityType.RACE)
                .entityId(race.getId());
            coverImage = fileRepository.save(coverImage);
            fileService.setFileUrl(coverImage);
            fileRepository.save(coverImage);
        }

        if(isPathMapImagePresent(raceDTO)) {
            File pathMapImage = new File()
                .mimeType(raceDTO.getBinaryPathMapContentType())
                .title(raceDTO.getBinaryPathMapFileName())
                .binary(raceDTO.getBinaryPathMapImage())
                .binaryContentType(raceDTO.getBinaryPathMapContentType())
                .type(FileType.PATH_IMAGE)
                .entityType(EntityType.RACE)
                .entityId(race.getId());
            pathMapImage = fileRepository.save(pathMapImage);
            fileService.setFileUrl(pathMapImage);
            fileRepository.save(pathMapImage);
        }

        Contact contact = new Contact()
            .name(raceDTO.getContactName())
            .email(raceDTO.getContactEmail())
            .phone(raceDTO.getContactPhone())
            .race(race);
        contactRepository.save(contact);

        final Race raceResult = raceSearchRepository.save(race);

        if(!isNew) {
            subscriptionTypeRepository.deleteByRaceId(raceResult.getId());

        }

        raceDTO.getSubscriptionTypes().stream()
            .map(subscriptionTypeMapper::toEntity)
            .map(subscriptionType -> subscriptionType.race(raceResult))
            .forEach(subscriptionTypeRepository::save);

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
            .map(this::setAttachment)
            .map(raceMapper::toDtoWithChildRelation);
    }

    @Transactional(readOnly = true)
    public Page<RaceDTO> findNextOrdered(LocalDate date, Pageable pageable) {

        log.debug("Request to get all Races");

        return raceRepository.findByGreaterDateOrderedByDate(date, pageable)
            .map(this::setAttachment)
            .map(raceMapper::toDtoWithChildRelation);
    }

    @Transactional(readOnly = true)
    public Page<RaceDTO> findNextByTeamOrdered(LocalDate date, Pageable pageable) {

        log.debug("Request to get all Races");

        Optional<AcsiTeamDTO> acsiTeam = acsiTeamService.getByLogin();

        if(acsiTeam.isPresent()) {
            return raceRepository.findByGreaterEqualDateAndTeamIdOrderedByDate(
                    date, acsiTeam.get().getId(), pageable)
                .map(this::setAttachment)
                .map(raceMapper::toDtoWithChildRelation);
        } else {
            return Page.empty();
        }
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
            .map(this::setAttachment)
            .map(raceMapper::toDtoWithChildRelation);
    }

    /**
     * Delete the race by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {

        log.debug("Request to delete Race : {}", id);

        raceRepository.findById(id).ifPresent(e -> {

            e.getAttachments().forEach(f -> {
                fileRepository.deleteById(f.getId());
            });

            e.getContacts().forEach(c -> {
                contactRepository.deleteById(c.getId());
            });

            subscriptionTypeRepository.deleteByRaceId(e.getId());

            raceRepository.deleteById(e.getId());
            raceSearchRepository.deleteById(e.getId());
        });
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

    private Race setAttachment(Race race) {
        return race.attachments(
            fileRepository.findByEntityTypeAndEntityId(
                EntityType.RACE, race.getId()));
    }

    private boolean isLogoImagePresent(RaceDTO raceDTO) {
        return (raceDTO != null &&
               raceDTO.getBinaryLogoImage() != null &&
               raceDTO.getBinaryLogoImage().length > 0 &&
               raceDTO.getBinaryLogoContentType() != null &&
               !raceDTO.getBinaryLogoContentType().isEmpty());
    }

    private boolean isCoverImagePresent(RaceDTO raceDTO) {
        return (raceDTO != null &&
            raceDTO.getBinaryCoverImage() != null &&
            raceDTO.getBinaryCoverImage().length > 0 &&
            raceDTO.getBinaryCoverContentType() != null &&
            !raceDTO.getBinaryCoverContentType().isEmpty());
    }

    private boolean isPathMapImagePresent(RaceDTO raceDTO) {
        return (raceDTO != null &&
            raceDTO.getBinaryPathMapImage() != null &&
            raceDTO.getBinaryPathMapImage().length > 0 &&
            raceDTO.getBinaryPathMapContentType() != null &&
            !raceDTO.getBinaryPathMapContentType().isEmpty());
    }
}
