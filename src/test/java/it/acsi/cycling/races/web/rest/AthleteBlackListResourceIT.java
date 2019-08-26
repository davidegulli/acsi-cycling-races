package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.AthleteBlackList;
import it.acsi.cycling.races.repository.AthleteBlackListRepository;
import it.acsi.cycling.races.repository.search.AthleteBlackListSearchRepository;
import it.acsi.cycling.races.service.AthleteBlackListService;
import it.acsi.cycling.races.service.dto.AthleteBlackListDTO;
import it.acsi.cycling.races.service.mapper.AthleteBlackListMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AthleteBlackListResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class AthleteBlackListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_BIRTH_DATE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_TAX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CODE = "BBBBBBBBBB";

    @Autowired
    private AthleteBlackListRepository athleteBlackListRepository;

    @Autowired
    private AthleteBlackListMapper athleteBlackListMapper;

    @Autowired
    private AthleteBlackListService athleteBlackListService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.AthleteBlackListSearchRepositoryMockConfiguration
     */
    @Autowired
    private AthleteBlackListSearchRepository mockAthleteBlackListSearchRepository;

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

    private MockMvc restAthleteBlackListMockMvc;

    private AthleteBlackList athleteBlackList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AthleteBlackListResource athleteBlackListResource = new AthleteBlackListResource(athleteBlackListService);
        this.restAthleteBlackListMockMvc = MockMvcBuilders.standaloneSetup(athleteBlackListResource)
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
    public static AthleteBlackList createEntity(EntityManager em) {
        AthleteBlackList athleteBlackList = new AthleteBlackList()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .taxCode(DEFAULT_TAX_CODE);
        return athleteBlackList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AthleteBlackList createUpdatedEntity(EntityManager em) {
        AthleteBlackList athleteBlackList = new AthleteBlackList()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .taxCode(UPDATED_TAX_CODE);
        return athleteBlackList;
    }

    @BeforeEach
    public void initTest() {
        athleteBlackList = createEntity(em);
    }

    @Test
    @Transactional
    public void createAthleteBlackList() throws Exception {
        int databaseSizeBeforeCreate = athleteBlackListRepository.findAll().size();

        // Create the AthleteBlackList
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);
        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isCreated());

        // Validate the AthleteBlackList in the database
        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeCreate + 1);
        AthleteBlackList testAthleteBlackList = athleteBlackListList.get(athleteBlackListList.size() - 1);
        assertThat(testAthleteBlackList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAthleteBlackList.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testAthleteBlackList.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAthleteBlackList.getTaxCode()).isEqualTo(DEFAULT_TAX_CODE);

        // Validate the AthleteBlackList in Elasticsearch
        verify(mockAthleteBlackListSearchRepository, times(1)).save(testAthleteBlackList);
    }

    @Test
    @Transactional
    public void createAthleteBlackListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = athleteBlackListRepository.findAll().size();

        // Create the AthleteBlackList with an existing ID
        athleteBlackList.setId(1L);
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AthleteBlackList in the database
        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeCreate);

        // Validate the AthleteBlackList in Elasticsearch
        verify(mockAthleteBlackListSearchRepository, times(0)).save(athleteBlackList);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteBlackListRepository.findAll().size();
        // set the field null
        athleteBlackList.setName(null);

        // Create the AthleteBlackList, which fails.
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteBlackListRepository.findAll().size();
        // set the field null
        athleteBlackList.setSurname(null);

        // Create the AthleteBlackList, which fails.
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteBlackListRepository.findAll().size();
        // set the field null
        athleteBlackList.setBirthDate(null);

        // Create the AthleteBlackList, which fails.
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteBlackListRepository.findAll().size();
        // set the field null
        athleteBlackList.setTaxCode(null);

        // Create the AthleteBlackList, which fails.
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        restAthleteBlackListMockMvc.perform(post("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAthleteBlackLists() throws Exception {
        // Initialize the database
        athleteBlackListRepository.saveAndFlush(athleteBlackList);

        // Get all the athleteBlackListList
        restAthleteBlackListMockMvc.perform(get("/api/athlete-black-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(athleteBlackList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE.toString())));
    }
    
    @Test
    @Transactional
    public void getAthleteBlackList() throws Exception {
        // Initialize the database
        athleteBlackListRepository.saveAndFlush(athleteBlackList);

        // Get the athleteBlackList
        restAthleteBlackListMockMvc.perform(get("/api/athlete-black-lists/{id}", athleteBlackList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(athleteBlackList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.taxCode").value(DEFAULT_TAX_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAthleteBlackList() throws Exception {
        // Get the athleteBlackList
        restAthleteBlackListMockMvc.perform(get("/api/athlete-black-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAthleteBlackList() throws Exception {
        // Initialize the database
        athleteBlackListRepository.saveAndFlush(athleteBlackList);

        int databaseSizeBeforeUpdate = athleteBlackListRepository.findAll().size();

        // Update the athleteBlackList
        AthleteBlackList updatedAthleteBlackList = athleteBlackListRepository.findById(athleteBlackList.getId()).get();
        // Disconnect from session so that the updates on updatedAthleteBlackList are not directly saved in db
        em.detach(updatedAthleteBlackList);
        updatedAthleteBlackList
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .taxCode(UPDATED_TAX_CODE);
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(updatedAthleteBlackList);

        restAthleteBlackListMockMvc.perform(put("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isOk());

        // Validate the AthleteBlackList in the database
        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeUpdate);
        AthleteBlackList testAthleteBlackList = athleteBlackListList.get(athleteBlackListList.size() - 1);
        assertThat(testAthleteBlackList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAthleteBlackList.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testAthleteBlackList.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAthleteBlackList.getTaxCode()).isEqualTo(UPDATED_TAX_CODE);

        // Validate the AthleteBlackList in Elasticsearch
        verify(mockAthleteBlackListSearchRepository, times(1)).save(testAthleteBlackList);
    }

    @Test
    @Transactional
    public void updateNonExistingAthleteBlackList() throws Exception {
        int databaseSizeBeforeUpdate = athleteBlackListRepository.findAll().size();

        // Create the AthleteBlackList
        AthleteBlackListDTO athleteBlackListDTO = athleteBlackListMapper.toDto(athleteBlackList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAthleteBlackListMockMvc.perform(put("/api/athlete-black-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athleteBlackListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AthleteBlackList in the database
        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AthleteBlackList in Elasticsearch
        verify(mockAthleteBlackListSearchRepository, times(0)).save(athleteBlackList);
    }

    @Test
    @Transactional
    public void deleteAthleteBlackList() throws Exception {
        // Initialize the database
        athleteBlackListRepository.saveAndFlush(athleteBlackList);

        int databaseSizeBeforeDelete = athleteBlackListRepository.findAll().size();

        // Delete the athleteBlackList
        restAthleteBlackListMockMvc.perform(delete("/api/athlete-black-lists/{id}", athleteBlackList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AthleteBlackList> athleteBlackListList = athleteBlackListRepository.findAll();
        assertThat(athleteBlackListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AthleteBlackList in Elasticsearch
        verify(mockAthleteBlackListSearchRepository, times(1)).deleteById(athleteBlackList.getId());
    }

    @Test
    @Transactional
    public void searchAthleteBlackList() throws Exception {
        // Initialize the database
        athleteBlackListRepository.saveAndFlush(athleteBlackList);
        when(mockAthleteBlackListSearchRepository.search(queryStringQuery("id:" + athleteBlackList.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(athleteBlackList), PageRequest.of(0, 1), 1));
        // Search the athleteBlackList
        restAthleteBlackListMockMvc.perform(get("/api/_search/athlete-black-lists?query=id:" + athleteBlackList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(athleteBlackList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AthleteBlackList.class);
        AthleteBlackList athleteBlackList1 = new AthleteBlackList();
        athleteBlackList1.setId(1L);
        AthleteBlackList athleteBlackList2 = new AthleteBlackList();
        athleteBlackList2.setId(athleteBlackList1.getId());
        assertThat(athleteBlackList1).isEqualTo(athleteBlackList2);
        athleteBlackList2.setId(2L);
        assertThat(athleteBlackList1).isNotEqualTo(athleteBlackList2);
        athleteBlackList1.setId(null);
        assertThat(athleteBlackList1).isNotEqualTo(athleteBlackList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AthleteBlackListDTO.class);
        AthleteBlackListDTO athleteBlackListDTO1 = new AthleteBlackListDTO();
        athleteBlackListDTO1.setId(1L);
        AthleteBlackListDTO athleteBlackListDTO2 = new AthleteBlackListDTO();
        assertThat(athleteBlackListDTO1).isNotEqualTo(athleteBlackListDTO2);
        athleteBlackListDTO2.setId(athleteBlackListDTO1.getId());
        assertThat(athleteBlackListDTO1).isEqualTo(athleteBlackListDTO2);
        athleteBlackListDTO2.setId(2L);
        assertThat(athleteBlackListDTO1).isNotEqualTo(athleteBlackListDTO2);
        athleteBlackListDTO1.setId(null);
        assertThat(athleteBlackListDTO1).isNotEqualTo(athleteBlackListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(athleteBlackListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(athleteBlackListMapper.fromId(null)).isNull();
    }
}
