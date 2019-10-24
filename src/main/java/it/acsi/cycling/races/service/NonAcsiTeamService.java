package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.NonAcsiTeam;
import it.acsi.cycling.races.repository.NonAcsiTeamRepository;
import it.acsi.cycling.races.repository.search.NonAcsiTeamSearchRepository;
import it.acsi.cycling.races.service.dto.NonAcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.NonAcsiTeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NonAcsiTeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NonAcsiTeams");
        return nonAcsiTeamRepository.findAll(pageable)
            .map(nonAcsiTeamMapper::toDto);
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

    @Transactional(readOnly = true)
    public Optional<NonAcsiTeamDTO> findByCode(String code) {

        log.debug("Request to get by code NonAcsiTeam : {}", code);

        return nonAcsiTeamRepository.findByCode(code)
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NonAcsiTeamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NonAcsiTeams for query {}", query);
        return nonAcsiTeamSearchRepository.search(queryStringQuery(query), pageable)
            .map(nonAcsiTeamMapper::toDto);
    }
}
