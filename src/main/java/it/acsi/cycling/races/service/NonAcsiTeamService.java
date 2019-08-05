package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.NonAcsiTeam;
import it.acsi.cycling.races.repository.NonAcsiTeamRepository;
import it.acsi.cycling.races.repository.search.NonAcsiTeamSearchRepository;
import it.acsi.cycling.races.service.dto.NonAcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.NonAcsiTeamMapper;
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
 * Service Implementation for managing {@link NonAcsiTeam}.
 */
@Service
@Transactional
public class NonAcsiTeamService {

    private final Logger log = LoggerFactory.getLogger(NonAcsiTeamService.class);

    private final NonAcsiTeamRepository nonAcsiTeamRepository;

    private final NonAcsiTeamMapper nonAcsiTeamMapper;

    private final NonAcsiTeamSearchRepository nonAcsiTeamSearchRepository;

    public NonAcsiTeamService(NonAcsiTeamRepository nonAcsiTeamRepository, NonAcsiTeamMapper nonAcsiTeamMapper, NonAcsiTeamSearchRepository nonAcsiTeamSearchRepository) {
        this.nonAcsiTeamRepository = nonAcsiTeamRepository;
        this.nonAcsiTeamMapper = nonAcsiTeamMapper;
        this.nonAcsiTeamSearchRepository = nonAcsiTeamSearchRepository;
    }

    /**
     * Save a nonAcsiTeam.
     *
     * @param nonAcsiTeamDTO the entity to save.
     * @return the persisted entity.
     */
    public NonAcsiTeamDTO save(NonAcsiTeamDTO nonAcsiTeamDTO) {
        log.debug("Request to save NonAcsiTeam : {}", nonAcsiTeamDTO);
        NonAcsiTeam nonAcsiTeam = nonAcsiTeamMapper.toEntity(nonAcsiTeamDTO);
        nonAcsiTeam = nonAcsiTeamRepository.save(nonAcsiTeam);
        NonAcsiTeamDTO result = nonAcsiTeamMapper.toDto(nonAcsiTeam);
        nonAcsiTeamSearchRepository.save(nonAcsiTeam);
        return result;
    }

    /**
     * Get all the nonAcsiTeams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NonAcsiTeamDTO> findAll() {
        log.debug("Request to get all NonAcsiTeams");
        return nonAcsiTeamRepository.findAll().stream()
            .map(nonAcsiTeamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one nonAcsiTeam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NonAcsiTeamDTO> findOne(Long id) {
        log.debug("Request to get NonAcsiTeam : {}", id);
        return nonAcsiTeamRepository.findById(id)
            .map(nonAcsiTeamMapper::toDto);
    }

    /**
     * Delete the nonAcsiTeam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NonAcsiTeam : {}", id);
        nonAcsiTeamRepository.deleteById(id);
        nonAcsiTeamSearchRepository.deleteById(id);
    }

    /**
     * Search for the nonAcsiTeam corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NonAcsiTeamDTO> search(String query) {
        log.debug("Request to search NonAcsiTeams for query {}", query);
        return StreamSupport
            .stream(nonAcsiTeamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(nonAcsiTeamMapper::toDto)
            .collect(Collectors.toList());
    }
}
