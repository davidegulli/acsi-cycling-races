package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.RaceType;
import it.acsi.cycling.races.repository.RaceTypeRepository;
import it.acsi.cycling.races.repository.search.RaceTypeSearchRepository;
import it.acsi.cycling.races.service.RaceTypeService;
import it.acsi.cycling.races.service.dto.RaceTypeDTO;
import it.acsi.cycling.races.service.mapper.RaceTypeMapper;
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
 * Integration tests for the {@link RaceTypeResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class RaceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RaceTypeRepository raceTypeRepository;

    @Autowired
    private RaceTypeMapper raceTypeMapper;

    @Autowired
    private RaceTypeService raceTypeService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.RaceTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private RaceTypeSearchRepository mockRaceTypeSearchRepository;

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

    private MockMvc restRaceTypeMockMvc;

    private RaceType raceType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RaceTypeResource raceTypeResource = new RaceTypeResource(raceTypeService);
        this.restRaceTypeMockMvc = MockMvcBuilders.standaloneSetup(raceTypeResource)
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
    public static RaceType createEntity(EntityManager em) {
        RaceType raceType = new RaceType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return raceType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaceType createUpdatedEntity(EntityManager em) {
        RaceType raceType = new RaceType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return raceType;
    }

    @BeforeEach
    public void initTest() {
        raceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRaceType() throws Exception {
        int databaseSizeBeforeCreate = raceTypeRepository.findAll().size();

        // Create the RaceType
        RaceTypeDTO raceTypeDTO = raceTypeMapper.toDto(raceType);
        restRaceTypeMockMvc.perform(post("/api/race-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RaceType in the database
        List<RaceType> raceTypeList = raceTypeRepository.findAll();
        assertThat(raceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RaceType testRaceType = raceTypeList.get(raceTypeList.size() - 1);
        assertThat(testRaceType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRaceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the RaceType in Elasticsearch
        verify(mockRaceTypeSearchRepository, times(1)).save(testRaceType);
    }

    @Test
    @Transactional
    public void createRaceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raceTypeRepository.findAll().size();

        // Create the RaceType with an existing ID
        raceType.setId(1L);
        RaceTypeDTO raceTypeDTO = raceTypeMapper.toDto(raceType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaceTypeMockMvc.perform(post("/api/race-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaceType in the database
        List<RaceType> raceTypeList = raceTypeRepository.findAll();
        assertThat(raceTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the RaceType in Elasticsearch
        verify(mockRaceTypeSearchRepository, times(0)).save(raceType);
    }


    @Test
    @Transactional
    public void getAllRaceTypes() throws Exception {
        // Initialize the database
        raceTypeRepository.saveAndFlush(raceType);

        // Get all the raceTypeList
        restRaceTypeMockMvc.perform(get("/api/race-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRaceType() throws Exception {
        // Initialize the database
        raceTypeRepository.saveAndFlush(raceType);

        // Get the raceType
        restRaceTypeMockMvc.perform(get("/api/race-types/{id}", raceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(raceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRaceType() throws Exception {
        // Get the raceType
        restRaceTypeMockMvc.perform(get("/api/race-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRaceType() throws Exception {
        // Initialize the database
        raceTypeRepository.saveAndFlush(raceType);

        int databaseSizeBeforeUpdate = raceTypeRepository.findAll().size();

        // Update the raceType
        RaceType updatedRaceType = raceTypeRepository.findById(raceType.getId()).get();
        // Disconnect from session so that the updates on updatedRaceType are not directly saved in db
        em.detach(updatedRaceType);
        updatedRaceType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        RaceTypeDTO raceTypeDTO = raceTypeMapper.toDto(updatedRaceType);

        restRaceTypeMockMvc.perform(put("/api/race-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceTypeDTO)))
            .andExpect(status().isOk());

        // Validate the RaceType in the database
        List<RaceType> raceTypeList = raceTypeRepository.findAll();
        assertThat(raceTypeList).hasSize(databaseSizeBeforeUpdate);
        RaceType testRaceType = raceTypeList.get(raceTypeList.size() - 1);
        assertThat(testRaceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRaceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the RaceType in Elasticsearch
        verify(mockRaceTypeSearchRepository, times(1)).save(testRaceType);
    }

    @Test
    @Transactional
    public void updateNonExistingRaceType() throws Exception {
        int databaseSizeBeforeUpdate = raceTypeRepository.findAll().size();

        // Create the RaceType
        RaceTypeDTO raceTypeDTO = raceTypeMapper.toDto(raceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceTypeMockMvc.perform(put("/api/race-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaceType in the database
        List<RaceType> raceTypeList = raceTypeRepository.findAll();
        assertThat(raceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RaceType in Elasticsearch
        verify(mockRaceTypeSearchRepository, times(0)).save(raceType);
    }

    @Test
    @Transactional
    public void deleteRaceType() throws Exception {
        // Initialize the database
        raceTypeRepository.saveAndFlush(raceType);

        int databaseSizeBeforeDelete = raceTypeRepository.findAll().size();

        // Delete the raceType
        restRaceTypeMockMvc.perform(delete("/api/race-types/{id}", raceType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RaceType> raceTypeList = raceTypeRepository.findAll();
        assertThat(raceTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RaceType in Elasticsearch
        verify(mockRaceTypeSearchRepository, times(1)).deleteById(raceType.getId());
    }

    @Test
    @Transactional
    public void searchRaceType() throws Exception {
        // Initialize the database
        raceTypeRepository.saveAndFlush(raceType);
        when(mockRaceTypeSearchRepository.search(queryStringQuery("id:" + raceType.getId())))
            .thenReturn(Collections.singletonList(raceType));
        // Search the raceType
        restRaceTypeMockMvc.perform(get("/api/_search/race-types?query=id:" + raceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceType.class);
        RaceType raceType1 = new RaceType();
        raceType1.setId(1L);
        RaceType raceType2 = new RaceType();
        raceType2.setId(raceType1.getId());
        assertThat(raceType1).isEqualTo(raceType2);
        raceType2.setId(2L);
        assertThat(raceType1).isNotEqualTo(raceType2);
        raceType1.setId(null);
        assertThat(raceType1).isNotEqualTo(raceType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceTypeDTO.class);
        RaceTypeDTO raceTypeDTO1 = new RaceTypeDTO();
        raceTypeDTO1.setId(1L);
        RaceTypeDTO raceTypeDTO2 = new RaceTypeDTO();
        assertThat(raceTypeDTO1).isNotEqualTo(raceTypeDTO2);
        raceTypeDTO2.setId(raceTypeDTO1.getId());
        assertThat(raceTypeDTO1).isEqualTo(raceTypeDTO2);
        raceTypeDTO2.setId(2L);
        assertThat(raceTypeDTO1).isNotEqualTo(raceTypeDTO2);
        raceTypeDTO1.setId(null);
        assertThat(raceTypeDTO1).isNotEqualTo(raceTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(raceTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(raceTypeMapper.fromId(null)).isNull();
    }
}
