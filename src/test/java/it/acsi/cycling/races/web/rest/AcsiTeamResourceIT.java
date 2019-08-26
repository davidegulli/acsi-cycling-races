package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.AcsiTeam;
import it.acsi.cycling.races.repository.AcsiTeamRepository;
import it.acsi.cycling.races.repository.search.AcsiTeamSearchRepository;
import it.acsi.cycling.races.service.AcsiTeamService;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.AcsiTeamMapper;
import it.acsi.cycling.races.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static it.acsi.cycling.races.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AcsiTeamResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class AcsiTeamResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private AcsiTeamRepository acsiTeamRepository;

    @Autowired
    private AcsiTeamMapper acsiTeamMapper;

    @Autowired
    private AcsiTeamService acsiTeamService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.AcsiTeamSearchRepositoryMockConfiguration
     */
    @Autowired
    private AcsiTeamSearchRepository mockAcsiTeamSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAcsiTeamMockMvc;

    private AcsiTeam acsiTeam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AcsiTeamResource acsiTeamResource = new AcsiTeamResource(acsiTeamService);
        this.restAcsiTeamMockMvc = MockMvcBuilders.standaloneSetup(acsiTeamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcsiTeam createEntity(EntityManager em) {
        AcsiTeam acsiTeam = new AcsiTeam()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .userId(DEFAULT_USER_ID);
        return acsiTeam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcsiTeam createUpdatedEntity(EntityManager em) {
        AcsiTeam acsiTeam = new AcsiTeam()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .userId(UPDATED_USER_ID);
        return acsiTeam;
    }

    @BeforeEach
    public void initTest() {
        acsiTeam = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcsiTeam() throws Exception {
        int databaseSizeBeforeCreate = acsiTeamRepository.findAll().size();

        // Create the AcsiTeam
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);
        restAcsiTeamMockMvc.perform(post("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the AcsiTeam in the database
        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeCreate + 1);
        AcsiTeam testAcsiTeam = acsiTeamList.get(acsiTeamList.size() - 1);
        assertThat(testAcsiTeam.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAcsiTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcsiTeam.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the AcsiTeam in Elasticsearch
        verify(mockAcsiTeamSearchRepository, times(1)).save(testAcsiTeam);
    }

    @Test
    @Transactional
    public void createAcsiTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acsiTeamRepository.findAll().size();

        // Create the AcsiTeam with an existing ID
        acsiTeam.setId(1L);
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcsiTeamMockMvc.perform(post("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AcsiTeam in the database
        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeCreate);

        // Validate the AcsiTeam in Elasticsearch
        verify(mockAcsiTeamSearchRepository, times(0)).save(acsiTeam);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = acsiTeamRepository.findAll().size();
        // set the field null
        acsiTeam.setCode(null);

        // Create the AcsiTeam, which fails.
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);

        restAcsiTeamMockMvc.perform(post("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isBadRequest());

        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = acsiTeamRepository.findAll().size();
        // set the field null
        acsiTeam.setName(null);

        // Create the AcsiTeam, which fails.
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);

        restAcsiTeamMockMvc.perform(post("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isBadRequest());

        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = acsiTeamRepository.findAll().size();
        // set the field null
        acsiTeam.setUserId(null);

        // Create the AcsiTeam, which fails.
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);

        restAcsiTeamMockMvc.perform(post("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isBadRequest());

        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAcsiTeams() throws Exception {
        // Initialize the database
        acsiTeamRepository.saveAndFlush(acsiTeam);

        // Get all the acsiTeamList
        restAcsiTeamMockMvc.perform(get("/api/acsi-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acsiTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getAcsiTeam() throws Exception {
        // Initialize the database
        acsiTeamRepository.saveAndFlush(acsiTeam);

        // Get the acsiTeam
        restAcsiTeamMockMvc.perform(get("/api/acsi-teams/{id}", acsiTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acsiTeam.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAcsiTeam() throws Exception {
        // Get the acsiTeam
        restAcsiTeamMockMvc.perform(get("/api/acsi-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcsiTeam() throws Exception {
        // Initialize the database
        acsiTeamRepository.saveAndFlush(acsiTeam);

        int databaseSizeBeforeUpdate = acsiTeamRepository.findAll().size();

        // Update the acsiTeam
        AcsiTeam updatedAcsiTeam = acsiTeamRepository.findById(acsiTeam.getId()).get();
        // Disconnect from session so that the updates on updatedAcsiTeam are not directly saved in db
        em.detach(updatedAcsiTeam);
        updatedAcsiTeam
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .userId(UPDATED_USER_ID);
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(updatedAcsiTeam);

        restAcsiTeamMockMvc.perform(put("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isOk());

        // Validate the AcsiTeam in the database
        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeUpdate);
        AcsiTeam testAcsiTeam = acsiTeamList.get(acsiTeamList.size() - 1);
        assertThat(testAcsiTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAcsiTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcsiTeam.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the AcsiTeam in Elasticsearch
        verify(mockAcsiTeamSearchRepository, times(1)).save(testAcsiTeam);
    }

    @Test
    @Transactional
    public void updateNonExistingAcsiTeam() throws Exception {
        int databaseSizeBeforeUpdate = acsiTeamRepository.findAll().size();

        // Create the AcsiTeam
        AcsiTeamDTO acsiTeamDTO = acsiTeamMapper.toDto(acsiTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcsiTeamMockMvc.perform(put("/api/acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acsiTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AcsiTeam in the database
        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcsiTeam in Elasticsearch
        verify(mockAcsiTeamSearchRepository, times(0)).save(acsiTeam);
    }

    @Test
    @Transactional
    public void deleteAcsiTeam() throws Exception {
        // Initialize the database
        acsiTeamRepository.saveAndFlush(acsiTeam);

        int databaseSizeBeforeDelete = acsiTeamRepository.findAll().size();

        // Delete the acsiTeam
        restAcsiTeamMockMvc.perform(delete("/api/acsi-teams/{id}", acsiTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcsiTeam> acsiTeamList = acsiTeamRepository.findAll();
        assertThat(acsiTeamList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AcsiTeam in Elasticsearch
        verify(mockAcsiTeamSearchRepository, times(1)).deleteById(acsiTeam.getId());
    }

    @Test
    @Transactional
    public void searchAcsiTeam() throws Exception {
        // Initialize the database
        acsiTeamRepository.saveAndFlush(acsiTeam);
        when(mockAcsiTeamSearchRepository.search(queryStringQuery("id:" + acsiTeam.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(acsiTeam), PageRequest.of(0, 1), 1));
        // Search the acsiTeam
        restAcsiTeamMockMvc.perform(get("/api/_search/acsi-teams?query=id:" + acsiTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acsiTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcsiTeam.class);
        AcsiTeam acsiTeam1 = new AcsiTeam();
        acsiTeam1.setId(1L);
        AcsiTeam acsiTeam2 = new AcsiTeam();
        acsiTeam2.setId(acsiTeam1.getId());
        assertThat(acsiTeam1).isEqualTo(acsiTeam2);
        acsiTeam2.setId(2L);
        assertThat(acsiTeam1).isNotEqualTo(acsiTeam2);
        acsiTeam1.setId(null);
        assertThat(acsiTeam1).isNotEqualTo(acsiTeam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcsiTeamDTO.class);
        AcsiTeamDTO acsiTeamDTO1 = new AcsiTeamDTO();
        acsiTeamDTO1.setId(1L);
        AcsiTeamDTO acsiTeamDTO2 = new AcsiTeamDTO();
        assertThat(acsiTeamDTO1).isNotEqualTo(acsiTeamDTO2);
        acsiTeamDTO2.setId(acsiTeamDTO1.getId());
        assertThat(acsiTeamDTO1).isEqualTo(acsiTeamDTO2);
        acsiTeamDTO2.setId(2L);
        assertThat(acsiTeamDTO1).isNotEqualTo(acsiTeamDTO2);
        acsiTeamDTO1.setId(null);
        assertThat(acsiTeamDTO1).isNotEqualTo(acsiTeamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(acsiTeamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(acsiTeamMapper.fromId(null)).isNull();
    }
}
