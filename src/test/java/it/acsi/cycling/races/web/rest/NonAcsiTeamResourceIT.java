package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.NonAcsiTeam;
import it.acsi.cycling.races.repository.NonAcsiTeamRepository;
import it.acsi.cycling.races.repository.search.NonAcsiTeamSearchRepository;
import it.acsi.cycling.races.service.NonAcsiTeamService;
import it.acsi.cycling.races.service.dto.NonAcsiTeamDTO;
import it.acsi.cycling.races.service.mapper.NonAcsiTeamMapper;
import it.acsi.cycling.races.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@Link NonAcsiTeamResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class NonAcsiTeamResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private NonAcsiTeamRepository nonAcsiTeamRepository;

    @Autowired
    private NonAcsiTeamMapper nonAcsiTeamMapper;

    @Autowired
    private NonAcsiTeamService nonAcsiTeamService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.NonAcsiTeamSearchRepositoryMockConfiguration
     */
    @Autowired
    private NonAcsiTeamSearchRepository mockNonAcsiTeamSearchRepository;

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

    private MockMvc restNonAcsiTeamMockMvc;

    private NonAcsiTeam nonAcsiTeam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NonAcsiTeamResource nonAcsiTeamResource = new NonAcsiTeamResource(nonAcsiTeamService);
        this.restNonAcsiTeamMockMvc = MockMvcBuilders.standaloneSetup(nonAcsiTeamResource)
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
    public static NonAcsiTeam createEntity(EntityManager em) {
        NonAcsiTeam nonAcsiTeam = new NonAcsiTeam()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return nonAcsiTeam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NonAcsiTeam createUpdatedEntity(EntityManager em) {
        NonAcsiTeam nonAcsiTeam = new NonAcsiTeam()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        return nonAcsiTeam;
    }

    @BeforeEach
    public void initTest() {
        nonAcsiTeam = createEntity(em);
    }

    @Test
    @Transactional
    public void createNonAcsiTeam() throws Exception {
        int databaseSizeBeforeCreate = nonAcsiTeamRepository.findAll().size();

        // Create the NonAcsiTeam
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(nonAcsiTeam);
        restNonAcsiTeamMockMvc.perform(post("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the NonAcsiTeam in the database
        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeCreate + 1);
        NonAcsiTeam testNonAcsiTeam = nonAcsiTeamList.get(nonAcsiTeamList.size() - 1);
        assertThat(testNonAcsiTeam.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testNonAcsiTeam.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the NonAcsiTeam in Elasticsearch
        verify(mockNonAcsiTeamSearchRepository, times(1)).save(testNonAcsiTeam);
    }

    @Test
    @Transactional
    public void createNonAcsiTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nonAcsiTeamRepository.findAll().size();

        // Create the NonAcsiTeam with an existing ID
        nonAcsiTeam.setId(1L);
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(nonAcsiTeam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNonAcsiTeamMockMvc.perform(post("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NonAcsiTeam in the database
        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeCreate);

        // Validate the NonAcsiTeam in Elasticsearch
        verify(mockNonAcsiTeamSearchRepository, times(0)).save(nonAcsiTeam);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nonAcsiTeamRepository.findAll().size();
        // set the field null
        nonAcsiTeam.setCode(null);

        // Create the NonAcsiTeam, which fails.
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(nonAcsiTeam);

        restNonAcsiTeamMockMvc.perform(post("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isBadRequest());

        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nonAcsiTeamRepository.findAll().size();
        // set the field null
        nonAcsiTeam.setName(null);

        // Create the NonAcsiTeam, which fails.
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(nonAcsiTeam);

        restNonAcsiTeamMockMvc.perform(post("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isBadRequest());

        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNonAcsiTeams() throws Exception {
        // Initialize the database
        nonAcsiTeamRepository.saveAndFlush(nonAcsiTeam);

        // Get all the nonAcsiTeamList
        restNonAcsiTeamMockMvc.perform(get("/api/non-acsi-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nonAcsiTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getNonAcsiTeam() throws Exception {
        // Initialize the database
        nonAcsiTeamRepository.saveAndFlush(nonAcsiTeam);

        // Get the nonAcsiTeam
        restNonAcsiTeamMockMvc.perform(get("/api/non-acsi-teams/{id}", nonAcsiTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nonAcsiTeam.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNonAcsiTeam() throws Exception {
        // Get the nonAcsiTeam
        restNonAcsiTeamMockMvc.perform(get("/api/non-acsi-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNonAcsiTeam() throws Exception {
        // Initialize the database
        nonAcsiTeamRepository.saveAndFlush(nonAcsiTeam);

        int databaseSizeBeforeUpdate = nonAcsiTeamRepository.findAll().size();

        // Update the nonAcsiTeam
        NonAcsiTeam updatedNonAcsiTeam = nonAcsiTeamRepository.findById(nonAcsiTeam.getId()).get();
        // Disconnect from session so that the updates on updatedNonAcsiTeam are not directly saved in db
        em.detach(updatedNonAcsiTeam);
        updatedNonAcsiTeam
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(updatedNonAcsiTeam);

        restNonAcsiTeamMockMvc.perform(put("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isOk());

        // Validate the NonAcsiTeam in the database
        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeUpdate);
        NonAcsiTeam testNonAcsiTeam = nonAcsiTeamList.get(nonAcsiTeamList.size() - 1);
        assertThat(testNonAcsiTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNonAcsiTeam.getName()).isEqualTo(UPDATED_NAME);

        // Validate the NonAcsiTeam in Elasticsearch
        verify(mockNonAcsiTeamSearchRepository, times(1)).save(testNonAcsiTeam);
    }

    @Test
    @Transactional
    public void updateNonExistingNonAcsiTeam() throws Exception {
        int databaseSizeBeforeUpdate = nonAcsiTeamRepository.findAll().size();

        // Create the NonAcsiTeam
        NonAcsiTeamDTO nonAcsiTeamDTO = nonAcsiTeamMapper.toDto(nonAcsiTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNonAcsiTeamMockMvc.perform(put("/api/non-acsi-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nonAcsiTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NonAcsiTeam in the database
        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonAcsiTeam in Elasticsearch
        verify(mockNonAcsiTeamSearchRepository, times(0)).save(nonAcsiTeam);
    }

    @Test
    @Transactional
    public void deleteNonAcsiTeam() throws Exception {
        // Initialize the database
        nonAcsiTeamRepository.saveAndFlush(nonAcsiTeam);

        int databaseSizeBeforeDelete = nonAcsiTeamRepository.findAll().size();

        // Delete the nonAcsiTeam
        restNonAcsiTeamMockMvc.perform(delete("/api/non-acsi-teams/{id}", nonAcsiTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NonAcsiTeam> nonAcsiTeamList = nonAcsiTeamRepository.findAll();
        assertThat(nonAcsiTeamList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NonAcsiTeam in Elasticsearch
        verify(mockNonAcsiTeamSearchRepository, times(1)).deleteById(nonAcsiTeam.getId());
    }

    @Test
    @Transactional
    public void searchNonAcsiTeam() throws Exception {
        // Initialize the database
        nonAcsiTeamRepository.saveAndFlush(nonAcsiTeam);
        when(mockNonAcsiTeamSearchRepository.search(queryStringQuery("id:" + nonAcsiTeam.getId())))
            .thenReturn(Collections.singletonList(nonAcsiTeam));
        // Search the nonAcsiTeam
        restNonAcsiTeamMockMvc.perform(get("/api/_search/non-acsi-teams?query=id:" + nonAcsiTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nonAcsiTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NonAcsiTeam.class);
        NonAcsiTeam nonAcsiTeam1 = new NonAcsiTeam();
        nonAcsiTeam1.setId(1L);
        NonAcsiTeam nonAcsiTeam2 = new NonAcsiTeam();
        nonAcsiTeam2.setId(nonAcsiTeam1.getId());
        assertThat(nonAcsiTeam1).isEqualTo(nonAcsiTeam2);
        nonAcsiTeam2.setId(2L);
        assertThat(nonAcsiTeam1).isNotEqualTo(nonAcsiTeam2);
        nonAcsiTeam1.setId(null);
        assertThat(nonAcsiTeam1).isNotEqualTo(nonAcsiTeam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NonAcsiTeamDTO.class);
        NonAcsiTeamDTO nonAcsiTeamDTO1 = new NonAcsiTeamDTO();
        nonAcsiTeamDTO1.setId(1L);
        NonAcsiTeamDTO nonAcsiTeamDTO2 = new NonAcsiTeamDTO();
        assertThat(nonAcsiTeamDTO1).isNotEqualTo(nonAcsiTeamDTO2);
        nonAcsiTeamDTO2.setId(nonAcsiTeamDTO1.getId());
        assertThat(nonAcsiTeamDTO1).isEqualTo(nonAcsiTeamDTO2);
        nonAcsiTeamDTO2.setId(2L);
        assertThat(nonAcsiTeamDTO1).isNotEqualTo(nonAcsiTeamDTO2);
        nonAcsiTeamDTO1.setId(null);
        assertThat(nonAcsiTeamDTO1).isNotEqualTo(nonAcsiTeamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(nonAcsiTeamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(nonAcsiTeamMapper.fromId(null)).isNull();
    }
}
