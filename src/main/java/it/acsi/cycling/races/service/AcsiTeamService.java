package it.acsi.cycling.races.service;

import it.acsi.cycling.races.domain.AcsiTeam;
import it.acsi.cycling.races.domain.User;
import it.acsi.cycling.races.repository.AcsiTeamRepository;
import it.acsi.cycling.races.repository.UserRepository;
import it.acsi.cycling.races.repository.search.AcsiTeamSearchRepository;
import it.acsi.cycling.races.security.SecurityUtils;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.AcsiTeamMapper;
import it.acsi.cycling.races.web.rest.errors.EmailAlreadyUsedException;
import it.acsi.cycling.races.web.rest.errors.LoginAlreadyUsedException;
import it.acsi.cycling.races.web.rest.errors.ServiceMomentlyNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AcsiTeamService(
        AcsiTeamRepository acsiTeamRepository,
        AcsiTeamMapper acsiTeamMapper,
        AcsiTeamSearchRepository acsiTeamSearchRepository,
        UserRepository userRepository,
        UserService userService,
        MailService mailService) {

        this.acsiTeamRepository = acsiTeamRepository;
        this.acsiTeamMapper = acsiTeamMapper;
        this.acsiTeamSearchRepository = acsiTeamSearchRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * Save a acsiTeam.
     *
     * @param acsiTeamDTO the entity to save.
     * @return the persisted entity.
     */
    public AcsiTeamDTO save(AcsiTeamDTO acsiTeamDTO) {

        log.debug("Request to save AcsiTeam : {}", acsiTeamDTO);

        if(acsiTeamDTO.getId() == null) {

            String login = new StringBuilder()
                .append(acsiTeamDTO.getManagerName())
                .append(".")
                .append(acsiTeamDTO.getManagerSurname())
                .toString();

            if (userRepository.findOneByLogin(login.toLowerCase()).isPresent()) {
                throw new LoginAlreadyUsedException();
            } else if (userRepository.findOneByEmailIgnoreCase(acsiTeamDTO.getManagerEmail()).isPresent()) {
                throw new EmailAlreadyUsedException();
            }

            User user = userService.createTeamManagerUser(
                login,
                acsiTeamDTO.getManagerName(),
                acsiTeamDTO.getManagerSurname(),
                acsiTeamDTO.getManagerEmail());

            try {
                mailService.sendCreationEmail(user);
            } catch (Exception exc) {
                throw new ServiceMomentlyNotAvailableException();
            }

            acsiTeamDTO.setUserId(String.valueOf(user.getId()));
        }

        AcsiTeam acsiTeam = acsiTeamMapper.toEntity(acsiTeamDTO);
        acsiTeam = acsiTeamRepository.save(acsiTeam);

        AcsiTeamDTO result = acsiTeamMapper.toDto(acsiTeam);
        acsiTeamSearchRepository.save(acsiTeam);

        return result;

    }

    /**
     * Get all the acsiTeams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AcsiTeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcsiTeams");
        return acsiTeamRepository.findAll(pageable)
            .map(acsiTeamMapper::toDto);
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
            .map(acsiTeamMapper::toDto)
            .map(e -> {
                AcsiTeamDTO result = new AcsiTeamDTO();
                result.setId(e.getId());
                result.setCode(e.getCode());
                result.setName(e.getName());
                result.setUserId(e.getUserId());

                userRepository.findById(Long.parseLong(e.getUserId()))
                    .ifPresent(user -> {
                        result.setManagerName(user.getFirstName());
                        result.setManagerSurname(user.getLastName());
                        result.setManagerEmail(user.getEmail());
                    });
                return result;
            });
    }

    @Transactional(readOnly = true)
    public Optional<AcsiTeamDTO> findByCode(String code) {

        log.debug("Request to get by code AcsiTeamDTO : {}", code);

        return acsiTeamRepository.findByCode(code)
            .map(acsiTeamMapper::toDto);
    }

    public Optional<AcsiTeamDTO> getByLogin() {

        Optional<User> user =
            SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);

        if(user.isPresent()) {
            return acsiTeamRepository.findByUserId(String.valueOf(user.get().getId()))
                .map(acsiTeamMapper::toDto);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Delete the acsiTeam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {

        log.debug("Request to delete AcsiTeam : {}", id);

        Optional<AcsiTeam> acsiTeam = acsiTeamRepository.findById(id);

        acsiTeam.ifPresent(e -> {
            userService.deleteUser(Long.parseLong(e.getUserId()));
        });

        acsiTeamRepository.deleteById(id);
        acsiTeamSearchRepository.deleteById(id);
    }

    /**
     * Search for the acsiTeam corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AcsiTeamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AcsiTeams for query {}", query);
        return acsiTeamSearchRepository.search(queryStringQuery(query), pageable)
            .map(acsiTeamMapper::toDto);
    }
}
