package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.AcsiTeam;
import it.acsi.cycling.races.repository.AcsiTeamRepository;
import it.acsi.cycling.races.repository.search.AcsiTeamSearchRepository;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.AcsiTeamMapper;
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
 * Service Implementation for managing {@link AcsiTeam}.
 */
@Service
@Transactional
public class AcsiTeamService {

    private final Logger log = LoggerFactory.getLogger(AcsiTeamService.class);

    private final AcsiTeamRepository acsiTeamRepository;

    private final AcsiTeamMapper acsiTeamMapper;

    private final AcsiTeamSearchRepository acsiTeamSearchRepository;

    public AcsiTeamService(AcsiTeamRepository acsiTeamRepository, AcsiTeamMapper acsiTeamMapper, AcsiTeamSearchRepository acsiTeamSearchRepository) {
        this.acsiTeamRepository = acsiTeamRepository;
        this.acsiTeamMapper = acsiTeamMapper;
        this.acsiTeamSearchRepository = acsiTeamSearchRepository;
    }

    /**
     * Save a acsiTeam.
     *
     * @param acsiTeamDTO the entity to save.
     * @return the persisted entity.
     */
    public AcsiTeamDTO save(AcsiTeamDTO acsiTeamDTO) {
        log.debug("Request to save AcsiTeam : {}", acsiTeamDTO);
        AcsiTeam acsiTeam = acsiTeamMapper.toEntity(acsiTeamDTO);
        acsiTeam = acsiTeamRepository.save(acsiTeam);
        AcsiTeamDTO result = acsiTeamMapper.toDto(acsiTeam);
        acsiTeamSearchRepository.save(acsiTeam);
        return result;
    }

    /**
     * Get all the acsiTeams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AcsiTeamDTO> findAll() {
        log.debug("Request to get all AcsiTeams");
        return acsiTeamRepository.findAll().stream()
            .map(acsiTeamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one acsiTeam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AcsiTeamDTO> findOne(Long id) {
        log.debug("Request to get AcsiTeam : {}", id);
        return acsiTeamRepository.findById(id)
            .map(acsiTeamMapper::toDto);
    }

    /**
     * Delete the acsiTeam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AcsiTeam : {}", id);
        acsiTeamRepository.deleteById(id);
        acsiTeamSearchRepository.deleteById(id);
    }

    /**
     * Search for the acsiTeam corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AcsiTeamDTO> search(String query) {
        log.debug("Request to search AcsiTeams for query {}", query);
        return StreamSupport
            .stream(acsiTeamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(acsiTeamMapper::toDto)
            .collect(Collectors.toList());
    }
}
