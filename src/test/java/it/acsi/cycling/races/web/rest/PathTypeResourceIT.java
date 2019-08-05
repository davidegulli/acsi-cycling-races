package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.PathType;
import it.acsi.cycling.races.repository.PathTypeRepository;
import it.acsi.cycling.races.repository.search.PathTypeSearchRepository;
import it.acsi.cycling.races.service.PathTypeService;
import it.acsi.cycling.races.service.dto.PathTypeDTO;
import it.acsi.cycling.races.service.mapper.PathTypeMapper;
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
 * Integration tests for the {@Link PathTypeResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class PathTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DISTANCE = "AAAAAAAAAA";
    private static final String UPDATED_DISTANCE = "BBBBBBBBBB";

    @Autowired
    private PathTypeRepository pathTypeRepository;

    @Autowired
    private PathTypeMapper pathTypeMapper;

    @Autowired
    private PathTypeService pathTypeService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.PathTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PathTypeSearchRepository mockPathTypeSearchRepository;

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

    private MockMvc restPathTypeMockMvc;

    private PathType pathType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PathTypeResource pathTypeResource = new PathTypeResource(pathTypeService);
        this.restPathTypeMockMvc = MockMvcBuilders.standaloneSetup(pathTypeResource)
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
    public static PathType createEntity(EntityManager em) {
        PathType pathType = new PathType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .distance(DEFAULT_DISTANCE);
        return pathType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PathType createUpdatedEntity(EntityManager em) {
        PathType pathType = new PathType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .distance(UPDATED_DISTANCE);
        return pathType;
    }

    @BeforeEach
    public void initTest() {
        pathType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPathType() throws Exception {
        int databaseSizeBeforeCreate = pathTypeRepository.findAll().size();

        // Create the PathType
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(pathType);
        restPathTypeMockMvc.perform(post("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PathType in the database
        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PathType testPathType = pathTypeList.get(pathTypeList.size() - 1);
        assertThat(testPathType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPathType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPathType.getDistance()).isEqualTo(DEFAULT_DISTANCE);

        // Validate the PathType in Elasticsearch
        verify(mockPathTypeSearchRepository, times(1)).save(testPathType);
    }

    @Test
    @Transactional
    public void createPathTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pathTypeRepository.findAll().size();

        // Create the PathType with an existing ID
        pathType.setId(1L);
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(pathType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPathTypeMockMvc.perform(post("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PathType in the database
        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the PathType in Elasticsearch
        verify(mockPathTypeSearchRepository, times(0)).save(pathType);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pathTypeRepository.findAll().size();
        // set the field null
        pathType.setName(null);

        // Create the PathType, which fails.
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(pathType);

        restPathTypeMockMvc.perform(post("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isBadRequest());

        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pathTypeRepository.findAll().size();
        // set the field null
        pathType.setDescription(null);

        // Create the PathType, which fails.
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(pathType);

        restPathTypeMockMvc.perform(post("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isBadRequest());

        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPathTypes() throws Exception {
        // Initialize the database
        pathTypeRepository.saveAndFlush(pathType);

        // Get all the pathTypeList
        restPathTypeMockMvc.perform(get("/api/path-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.toString())));
    }
    
    @Test
    @Transactional
    public void getPathType() throws Exception {
        // Initialize the database
        pathTypeRepository.saveAndFlush(pathType);

        // Get the pathType
        restPathTypeMockMvc.perform(get("/api/path-types/{id}", pathType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pathType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPathType() throws Exception {
        // Get the pathType
        restPathTypeMockMvc.perform(get("/api/path-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePathType() throws Exception {
        // Initialize the database
        pathTypeRepository.saveAndFlush(pathType);

        int databaseSizeBeforeUpdate = pathTypeRepository.findAll().size();

        // Update the pathType
        PathType updatedPathType = pathTypeRepository.findById(pathType.getId()).get();
        // Disconnect from session so that the updates on updatedPathType are not directly saved in db
        em.detach(updatedPathType);
        updatedPathType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .distance(UPDATED_DISTANCE);
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(updatedPathType);

        restPathTypeMockMvc.perform(put("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isOk());

        // Validate the PathType in the database
        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeUpdate);
        PathType testPathType = pathTypeList.get(pathTypeList.size() - 1);
        assertThat(testPathType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPathType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPathType.getDistance()).isEqualTo(UPDATED_DISTANCE);

        // Validate the PathType in Elasticsearch
        verify(mockPathTypeSearchRepository, times(1)).save(testPathType);
    }

    @Test
    @Transactional
    public void updateNonExistingPathType() throws Exception {
        int databaseSizeBeforeUpdate = pathTypeRepository.findAll().size();

        // Create the PathType
        PathTypeDTO pathTypeDTO = pathTypeMapper.toDto(pathType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPathTypeMockMvc.perform(put("/api/path-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PathType in the database
        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PathType in Elasticsearch
        verify(mockPathTypeSearchRepository, times(0)).save(pathType);
    }

    @Test
    @Transactional
    public void deletePathType() throws Exception {
        // Initialize the database
        pathTypeRepository.saveAndFlush(pathType);

        int databaseSizeBeforeDelete = pathTypeRepository.findAll().size();

        // Delete the pathType
        restPathTypeMockMvc.perform(delete("/api/path-types/{id}", pathType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PathType> pathTypeList = pathTypeRepository.findAll();
        assertThat(pathTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PathType in Elasticsearch
        verify(mockPathTypeSearchRepository, times(1)).deleteById(pathType.getId());
    }

    @Test
    @Transactional
    public void searchPathType() throws Exception {
        // Initialize the database
        pathTypeRepository.saveAndFlush(pathType);
        when(mockPathTypeSearchRepository.search(queryStringQuery("id:" + pathType.getId())))
            .thenReturn(Collections.singletonList(pathType));
        // Search the pathType
        restPathTypeMockMvc.perform(get("/api/_search/path-types?query=id:" + pathType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathType.class);
        PathType pathType1 = new PathType();
        pathType1.setId(1L);
        PathType pathType2 = new PathType();
        pathType2.setId(pathType1.getId());
        assertThat(pathType1).isEqualTo(pathType2);
        pathType2.setId(2L);
        assertThat(pathType1).isNotEqualTo(pathType2);
        pathType1.setId(null);
        assertThat(pathType1).isNotEqualTo(pathType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathTypeDTO.class);
        PathTypeDTO pathTypeDTO1 = new PathTypeDTO();
        pathTypeDTO1.setId(1L);
        PathTypeDTO pathTypeDTO2 = new PathTypeDTO();
        assertThat(pathTypeDTO1).isNotEqualTo(pathTypeDTO2);
        pathTypeDTO2.setId(pathTypeDTO1.getId());
        assertThat(pathTypeDTO1).isEqualTo(pathTypeDTO2);
        pathTypeDTO2.setId(2L);
        assertThat(pathTypeDTO1).isNotEqualTo(pathTypeDTO2);
        pathTypeDTO1.setId(null);
        assertThat(pathTypeDTO1).isNotEqualTo(pathTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pathTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pathTypeMapper.fromId(null)).isNull();
    }
}
