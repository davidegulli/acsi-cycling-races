package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.AcsiTeam;
import it.acsi.cycling.races.domain.NonAcsiTeam;
import it.acsi.cycling.races.domain.RaceSubscription;
import it.acsi.cycling.races.repository.AcsiTeamRepository;
import it.acsi.cycling.races.repository.NonAcsiTeamRepository;
import it.acsi.cycling.races.repository.RaceSubscriptionRepository;
import it.acsi.cycling.races.repository.search.RaceSubscriptionSearchRepository;
import it.acsi.cycling.races.service.dto.*;
import it.acsi.cycling.races.service.mapper.NonAcsiTeamMapper;
import it.acsi.cycling.races.service.mapper.RaceSubscriptionMapper;
import it.acsi.cycling.races.service.report.ExcelReportHandler;
import it.acsi.cycling.races.service.report.PdfReportHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link RaceSubscription}.
 */
@Service
@Transactional
public class RaceSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(RaceSubscriptionService.class);

    private final RaceSubscriptionRepository raceSubscriptionRepository;
    private final AcsiTeamRepository acsiTeamRepository;
    private final NonAcsiTeamRepository nonAcsiTeamRepository;

    private final RaceSubscriptionMapper raceSubscriptionMapper;
    private final NonAcsiTeamMapper nonAcsiTeamMapper;

    private final RaceSubscriptionSearchRepository raceSubscriptionSearchRepository;

    private final AcsiTeamService acsiTeamService;
    private final NonAcsiTeamService nonAcsiTeamService;

    private final ExcelReportHandler excelReportHandler;
    private final PdfReportHandler pdfReportHandler;

    public RaceSubscriptionService(
        RaceSubscriptionRepository raceSubscriptionRepository,
        AcsiTeamRepository acsiTeamRepository,
        NonAcsiTeamRepository nonAcsiTeamRepository,
        RaceSubscriptionMapper raceSubscriptionMapper,
        NonAcsiTeamMapper nonAcsiTeamMapper,
        RaceSubscriptionSearchRepository raceSubscriptionSearchRepository,
        AcsiTeamService acsiTeamService,
        NonAcsiTeamService nonAcsiTeamService,
        ExcelReportHandler excelReportHandler,
        PdfReportHandler pdfReportHandler) {

        this.raceSubscriptionRepository = raceSubscriptionRepository;
        this.acsiTeamRepository = acsiTeamRepository;
        this.nonAcsiTeamRepository = nonAcsiTeamRepository;
        this.raceSubscriptionMapper = raceSubscriptionMapper;
        this.nonAcsiTeamMapper = nonAcsiTeamMapper;
        this.raceSubscriptionSearchRepository = raceSubscriptionSearchRepository;
        this.acsiTeamService = acsiTeamService;
        this.nonAcsiTeamService = nonAcsiTeamService;
        this.excelReportHandler = excelReportHandler;
        this.pdfReportHandler = pdfReportHandler;
    }

    /**
     * Save a raceSubscription.
     *
     * @param raceSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public RaceSubscriptionDTO save(RaceSubscriptionDTO raceSubscriptionDTO) {

        log.debug("Request to save RaceSubscription : {}", raceSubscriptionDTO);

        Long teamId = handleTeam(raceSubscriptionDTO);

        RaceSubscription raceSubscription = raceSubscriptionMapper.toEntity(raceSubscriptionDTO);
        raceSubscription.setDate(Instant.now());
        raceSubscription.setTeamId(teamId);
        raceSubscription.setPathTypeId(raceSubscriptionDTO.getPathTypeId());

        raceSubscription = raceSubscriptionRepository.save(raceSubscription);

        RaceSubscriptionDTO result = raceSubscriptionMapper.toDto(raceSubscription);
        raceSubscriptionSearchRepository.save(raceSubscription);

        return result;
    }

    private Long handleTeam(RaceSubscriptionDTO raceSubscriptionDTO) {

        if(raceSubscriptionDTO.getTeamId() != 0) {
           return raceSubscriptionDTO.getTeamId();
        }

        Optional<AcsiTeamDTO> acsiTeamDTO = acsiTeamService.findByCode(raceSubscriptionDTO.getTeamCode());
        if(acsiTeamDTO.isPresent()) {
            return acsiTeamDTO.get().getId();
        }

        Optional<NonAcsiTeamDTO> nonAcsiTeamDTO = nonAcsiTeamService.findByCode(raceSubscriptionDTO.getTeamCode());
        if(nonAcsiTeamDTO.isPresent()) {
            return nonAcsiTeamDTO.get().getId();
        }

        NonAcsiTeamDTO newNonAcsiTeamDTO = new NonAcsiTeamDTO();
        newNonAcsiTeamDTO.setCode(raceSubscriptionDTO.getTeamCode());
        newNonAcsiTeamDTO.setName(raceSubscriptionDTO.getTeamName());

        newNonAcsiTeamDTO = nonAcsiTeamService.save(newNonAcsiTeamDTO);

        return newNonAcsiTeamDTO.getId();

    }

    /**
     * Get all the raceSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceSubscriptionDTO> findAll(Pageable pageable) {

        log.debug("Request to get all RaceSubscriptions");

        return raceSubscriptionRepository.findAll(pageable)
            .map(raceSubscriptionMapper::toDto)
            .map(this::setAcsiTeam);
    }

    @Transactional(readOnly = true)
    public Page<RaceSubscriptionDTO> findByRace(Long raceId, Pageable pageable) {

        log.debug("Request to get all RaceSubscriptions");

        return raceSubscriptionRepository.findByRaceId(raceId, pageable)
            .map(raceSubscriptionMapper::toDto)
            .map(this::setAcsiTeam);
    }

    @Transactional(readOnly = true)
    public byte[] generateExcelReportByRace(Long raceId) {

        log.debug("Request to get all RaceSubscriptions");

        List<RaceSubscriptionDTO> results = raceSubscriptionRepository.findByRaceId(raceId)
            .map(raceSubscriptionMapper::toDto)
            .map(this::setAcsiTeam)
            .collect(Collectors.toList());

        return excelReportHandler.buildRaceSubscriptionExcelReport(results);
    }

    @Transactional(readOnly = true)
    public byte[] generatePdfReportByRace(Long raceId) {

        log.debug("Request to get all RaceSubscriptions");

        List<RaceSubscriptionDTO> results = raceSubscriptionRepository.findByRaceId(raceId)
            .map(raceSubscriptionMapper::toDto)
            .map(this::setAcsiTeam)
            .collect(Collectors.toList());

        return pdfReportHandler.buildRaceSubscriptionPdfReport(results);
    }

    @Transactional(readOnly = true)
    public List<TeamDTO> searchTeamByInitial(String codeInitial) {

        log.debug("Request to search Team By Initial");

        Stream<TeamDTO> acsiTeams = acsiTeamRepository.searchByCodeInitial(codeInitial).stream()
            .map(team -> {
                TeamDTO result = new TeamDTO();
                result.setId(team.getId());
                result.setCode(team.getCode());
                result.setName(team.getName());
                return result;
            });

        Stream<TeamDTO> nonAcsiTeams = nonAcsiTeamRepository.searchByCodeInitial(codeInitial).stream()
            .map(team -> {
                TeamDTO result = new TeamDTO();
                result.setId(team.getId());
                result.setCode(team.getCode());
                result.setName(team.getName());
                return result;
            });

        return Stream.concat(acsiTeams, nonAcsiTeams)
            .collect(Collectors.toList());
    }

    /**
     * Get one raceSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaceSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get RaceSubscription : {}", id);
        return raceSubscriptionRepository.findById(id)
            .map(raceSubscriptionMapper::toDto);
    }

    /**
     * Delete the raceSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RaceSubscription : {}", id);
        raceSubscriptionRepository.deleteById(id);
        raceSubscriptionSearchRepository.deleteById(id);
    }

    /**
     * Search for the raceSubscription corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaceSubscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RaceSubscriptions for query {}", query);
        return raceSubscriptionSearchRepository.search(queryStringQuery(query), pageable)
            .map(raceSubscriptionMapper::toDto);
    }

    private RaceSubscriptionDTO setAcsiTeam(RaceSubscriptionDTO raceSubscriptionDTO) {

        Optional<AcsiTeamDTO> team =
            acsiTeamService.findOne(raceSubscriptionDTO.getTeamId());

        if(team.isPresent()) {
            raceSubscriptionDTO.setTeamCode(team.get().getCode());
            raceSubscriptionDTO.setTeamName(team.get().getName());
        }

        return raceSubscriptionDTO;
    }

}
