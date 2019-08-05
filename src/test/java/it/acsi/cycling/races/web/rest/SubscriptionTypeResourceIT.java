package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.SubscriptionType;
import it.acsi.cycling.races.repository.SubscriptionTypeRepository;
import it.acsi.cycling.races.repository.search.SubscriptionTypeSearchRepository;
import it.acsi.cycling.races.service.SubscriptionTypeService;
import it.acsi.cycling.races.service.dto.SubscriptionTypeDTO;
import it.acsi.cycling.races.service.mapper.SubscriptionTypeMapper;
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
 * Integration tests for the {@Link SubscriptionTypeResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class SubscriptionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RULES = "AAAAAAAAAA";
    private static final String UPDATED_RULES = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Autowired
    private SubscriptionTypeMapper subscriptionTypeMapper;

    @Autowired
    private SubscriptionTypeService subscriptionTypeService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.SubscriptionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubscriptionTypeSearchRepository mockSubscriptionTypeSearchRepository;

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

    private MockMvc restSubscriptionTypeMockMvc;

    private SubscriptionType subscriptionType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriptionTypeResource subscriptionTypeResource = new SubscriptionTypeResource(subscriptionTypeService);
        this.restSubscriptionTypeMockMvc = MockMvcBuilders.standaloneSetup(subscriptionTypeResource)
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
    public static SubscriptionType createEntity(EntityManager em) {
        SubscriptionType subscriptionType = new SubscriptionType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .rules(DEFAULT_RULES)
            .price(DEFAULT_PRICE);
        return subscriptionType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionType createUpdatedEntity(EntityManager em) {
        SubscriptionType subscriptionType = new SubscriptionType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .rules(UPDATED_RULES)
            .price(UPDATED_PRICE);
        return subscriptionType;
    }

    @BeforeEach
    public void initTest() {
        subscriptionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionType() throws Exception {
        int databaseSizeBeforeCreate = subscriptionTypeRepository.findAll().size();

        // Create the SubscriptionType
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(subscriptionType);
        restSubscriptionTypeMockMvc.perform(post("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionType in the database
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionType testSubscriptionType = subscriptionTypeList.get(subscriptionTypeList.size() - 1);
        assertThat(testSubscriptionType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubscriptionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubscriptionType.getRules()).isEqualTo(DEFAULT_RULES);
        assertThat(testSubscriptionType.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the SubscriptionType in Elasticsearch
        verify(mockSubscriptionTypeSearchRepository, times(1)).save(testSubscriptionType);
    }

    @Test
    @Transactional
    public void createSubscriptionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionTypeRepository.findAll().size();

        // Create the SubscriptionType with an existing ID
        subscriptionType.setId(1L);
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(subscriptionType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionTypeMockMvc.perform(post("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SubscriptionType in Elasticsearch
        verify(mockSubscriptionTypeSearchRepository, times(0)).save(subscriptionType);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionTypeRepository.findAll().size();
        // set the field null
        subscriptionType.setName(null);

        // Create the SubscriptionType, which fails.
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(subscriptionType);

        restSubscriptionTypeMockMvc.perform(post("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionTypeRepository.findAll().size();
        // set the field null
        subscriptionType.setDescription(null);

        // Create the SubscriptionType, which fails.
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(subscriptionType);

        restSubscriptionTypeMockMvc.perform(post("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionTypes() throws Exception {
        // Initialize the database
        subscriptionTypeRepository.saveAndFlush(subscriptionType);

        // Get all the subscriptionTypeList
        restSubscriptionTypeMockMvc.perform(get("/api/subscription-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionType() throws Exception {
        // Initialize the database
        subscriptionTypeRepository.saveAndFlush(subscriptionType);

        // Get the subscriptionType
        restSubscriptionTypeMockMvc.perform(get("/api/subscription-types/{id}", subscriptionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.rules").value(DEFAULT_RULES.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubscriptionType() throws Exception {
        // Get the subscriptionType
        restSubscriptionTypeMockMvc.perform(get("/api/subscription-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionType() throws Exception {
        // Initialize the database
        subscriptionTypeRepository.saveAndFlush(subscriptionType);

        int databaseSizeBeforeUpdate = subscriptionTypeRepository.findAll().size();

        // Update the subscriptionType
        SubscriptionType updatedSubscriptionType = subscriptionTypeRepository.findById(subscriptionType.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionType are not directly saved in db
        em.detach(updatedSubscriptionType);
        updatedSubscriptionType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .rules(UPDATED_RULES)
            .price(UPDATED_PRICE);
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(updatedSubscriptionType);

        restSubscriptionTypeMockMvc.perform(put("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionType in the database
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionType testSubscriptionType = subscriptionTypeList.get(subscriptionTypeList.size() - 1);
        assertThat(testSubscriptionType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubscriptionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubscriptionType.getRules()).isEqualTo(UPDATED_RULES);
        assertThat(testSubscriptionType.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the SubscriptionType in Elasticsearch
        verify(mockSubscriptionTypeSearchRepository, times(1)).save(testSubscriptionType);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionType() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionTypeRepository.findAll().size();

        // Create the SubscriptionType
        SubscriptionTypeDTO subscriptionTypeDTO = subscriptionTypeMapper.toDto(subscriptionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc.perform(put("/api/subscription-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubscriptionType in Elasticsearch
        verify(mockSubscriptionTypeSearchRepository, times(0)).save(subscriptionType);
    }

    @Test
    @Transactional
    public void deleteSubscriptionType() throws Exception {
        // Initialize the database
        subscriptionTypeRepository.saveAndFlush(subscriptionType);

        int databaseSizeBeforeDelete = subscriptionTypeRepository.findAll().size();

        // Delete the subscriptionType
        restSubscriptionTypeMockMvc.perform(delete("/api/subscription-types/{id}", subscriptionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();
        assertThat(subscriptionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SubscriptionType in Elasticsearch
        verify(mockSubscriptionTypeSearchRepository, times(1)).deleteById(subscriptionType.getId());
    }

    @Test
    @Transactional
    public void searchSubscriptionType() throws Exception {
        // Initialize the database
        subscriptionTypeRepository.saveAndFlush(subscriptionType);
        when(mockSubscriptionTypeSearchRepository.search(queryStringQuery("id:" + subscriptionType.getId())))
            .thenReturn(Collections.singletonList(subscriptionType));
        // Search the subscriptionType
        restSubscriptionTypeMockMvc.perform(get("/api/_search/subscription-types?query=id:" + subscriptionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionType.class);
        SubscriptionType subscriptionType1 = new SubscriptionType();
        subscriptionType1.setId(1L);
        SubscriptionType subscriptionType2 = new SubscriptionType();
        subscriptionType2.setId(subscriptionType1.getId());
        assertThat(subscriptionType1).isEqualTo(subscriptionType2);
        subscriptionType2.setId(2L);
        assertThat(subscriptionType1).isNotEqualTo(subscriptionType2);
        subscriptionType1.setId(null);
        assertThat(subscriptionType1).isNotEqualTo(subscriptionType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionTypeDTO.class);
        SubscriptionTypeDTO subscriptionTypeDTO1 = new SubscriptionTypeDTO();
        subscriptionTypeDTO1.setId(1L);
        SubscriptionTypeDTO subscriptionTypeDTO2 = new SubscriptionTypeDTO();
        assertThat(subscriptionTypeDTO1).isNotEqualTo(subscriptionTypeDTO2);
        subscriptionTypeDTO2.setId(subscriptionTypeDTO1.getId());
        assertThat(subscriptionTypeDTO1).isEqualTo(subscriptionTypeDTO2);
        subscriptionTypeDTO2.setId(2L);
        assertThat(subscriptionTypeDTO1).isNotEqualTo(subscriptionTypeDTO2);
        subscriptionTypeDTO1.setId(null);
        assertThat(subscriptionTypeDTO1).isNotEqualTo(subscriptionTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subscriptionTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subscriptionTypeMapper.fromId(null)).isNull();
    }
}
