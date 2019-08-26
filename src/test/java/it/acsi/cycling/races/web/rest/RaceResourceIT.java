package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.Race;
import it.acsi.cycling.races.repository.RaceRepository;
import it.acsi.cycling.races.repository.search.RaceSearchRepository;
import it.acsi.cycling.races.service.RaceService;
import it.acsi.cycling.races.service.dto.RaceDTO;
import it.acsi.cycling.races.service.mapper.RaceMapper;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
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

import it.acsi.cycling.races.domain.enumeration.RaceStatus;
/**
 * Integration tests for the {@link RaceResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class RaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final String DEFAULT_RULES = "AAAAAAAAAA";
    private static final String UPDATED_RULES = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBSCRIPTION_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_SUBSCRIPTION_EXPIRATION_DATE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_ATTRIBUTES = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTES = "BBBBBBBBBB";

    private static final RaceStatus DEFAULT_STATUS = RaceStatus.PUBLISHED;
    private static final RaceStatus UPDATED_STATUS = RaceStatus.DRAFT;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private RaceMapper raceMapper;

    @Autowired
    private RaceService raceService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.RaceSearchRepositoryMockConfiguration
     */
    @Autowired
    private RaceSearchRepository mockRaceSearchRepository;

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

    private MockMvc restRaceMockMvc;

    private Race race;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RaceResource raceResource = new RaceResource(raceService);
        this.restRaceMockMvc = MockMvcBuilders.standaloneSetup(raceResource)
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
    public static Race createEntity(EntityManager em) {
        Race race = new Race()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .location(DEFAULT_LOCATION)
            .description(DEFAULT_DESCRIPTION)
            .info(DEFAULT_INFO)
            .address(DEFAULT_ADDRESS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .rules(DEFAULT_RULES)
            .subscriptionExpirationDate(DEFAULT_SUBSCRIPTION_EXPIRATION_DATE)
            .attributes(DEFAULT_ATTRIBUTES)
            .status(DEFAULT_STATUS);
        return race;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Race createUpdatedEntity(EntityManager em) {
        Race race = new Race()
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .info(UPDATED_INFO)
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .rules(UPDATED_RULES)
            .subscriptionExpirationDate(UPDATED_SUBSCRIPTION_EXPIRATION_DATE)
            .attributes(UPDATED_ATTRIBUTES)
            .status(UPDATED_STATUS);
        return race;
    }

    @BeforeEach
    public void initTest() {
        race = createEntity(em);
    }

    @Test
    @Transactional
    public void createRace() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race
        RaceDTO raceDTO = raceMapper.toDto(race);
        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isCreated());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeCreate + 1);
        Race testRace = raceList.get(raceList.size() - 1);
        assertThat(testRace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRace.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRace.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testRace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRace.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testRace.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRace.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testRace.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testRace.getRules()).isEqualTo(DEFAULT_RULES);
        assertThat(testRace.getSubscriptionExpirationDate()).isEqualTo(DEFAULT_SUBSCRIPTION_EXPIRATION_DATE);
        assertThat(testRace.getAttributes()).isEqualTo(DEFAULT_ATTRIBUTES);
        assertThat(testRace.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Race in Elasticsearch
        verify(mockRaceSearchRepository, times(1)).save(testRace);
    }

    @Test
    @Transactional
    public void createRaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race with an existing ID
        race.setId(1L);
        RaceDTO raceDTO = raceMapper.toDto(race);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Race in Elasticsearch
        verify(mockRaceSearchRepository, times(0)).save(race);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceRepository.findAll().size();
        // set the field null
        race.setName(null);

        // Create the Race, which fails.
        RaceDTO raceDTO = raceMapper.toDto(race);

        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceRepository.findAll().size();
        // set the field null
        race.setDate(null);

        // Create the Race, which fails.
        RaceDTO raceDTO = raceMapper.toDto(race);

        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceRepository.findAll().size();
        // set the field null
        race.setLocation(null);

        // Create the Race, which fails.
        RaceDTO raceDTO = raceMapper.toDto(race);

        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceRepository.findAll().size();
        // set the field null
        race.setAddress(null);

        // Create the Race, which fails.
        RaceDTO raceDTO = raceMapper.toDto(race);

        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRaces() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get all the raceList
        restRaceMockMvc.perform(get("/api/races?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(race.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())))
            .andExpect(jsonPath("$.[*].subscriptionExpirationDate").value(hasItem(DEFAULT_SUBSCRIPTION_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].attributes").value(hasItem(DEFAULT_ATTRIBUTES.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", race.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(race.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.rules").value(DEFAULT_RULES.toString()))
            .andExpect(jsonPath("$.subscriptionExpirationDate").value(DEFAULT_SUBSCRIPTION_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.attributes").value(DEFAULT_ATTRIBUTES.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRace() throws Exception {
        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Update the race
        Race updatedRace = raceRepository.findById(race.getId()).get();
        // Disconnect from session so that the updates on updatedRace are not directly saved in db
        em.detach(updatedRace);
        updatedRace
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .info(UPDATED_INFO)
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .rules(UPDATED_RULES)
            .subscriptionExpirationDate(UPDATED_SUBSCRIPTION_EXPIRATION_DATE)
            .attributes(UPDATED_ATTRIBUTES)
            .status(UPDATED_STATUS);
        RaceDTO raceDTO = raceMapper.toDto(updatedRace);

        restRaceMockMvc.perform(put("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isOk());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeUpdate);
        Race testRace = raceList.get(raceList.size() - 1);
        assertThat(testRace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRace.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRace.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testRace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRace.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testRace.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRace.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testRace.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testRace.getRules()).isEqualTo(UPDATED_RULES);
        assertThat(testRace.getSubscriptionExpirationDate()).isEqualTo(UPDATED_SUBSCRIPTION_EXPIRATION_DATE);
        assertThat(testRace.getAttributes()).isEqualTo(UPDATED_ATTRIBUTES);
        assertThat(testRace.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Race in Elasticsearch
        verify(mockRaceSearchRepository, times(1)).save(testRace);
    }

    @Test
    @Transactional
    public void updateNonExistingRace() throws Exception {
        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Create the Race
        RaceDTO raceDTO = raceMapper.toDto(race);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceMockMvc.perform(put("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Race in Elasticsearch
        verify(mockRaceSearchRepository, times(0)).save(race);
    }

    @Test
    @Transactional
    public void deleteRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        int databaseSizeBeforeDelete = raceRepository.findAll().size();

        // Delete the race
        restRaceMockMvc.perform(delete("/api/races/{id}", race.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Race in Elasticsearch
        verify(mockRaceSearchRepository, times(1)).deleteById(race.getId());
    }

    @Test
    @Transactional
    public void searchRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);
        when(mockRaceSearchRepository.search(queryStringQuery("id:" + race.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(race), PageRequest.of(0, 1), 1));
        // Search the race
        restRaceMockMvc.perform(get("/api/_search/races?query=id:" + race.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(race.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES)))
            .andExpect(jsonPath("$.[*].subscriptionExpirationDate").value(hasItem(DEFAULT_SUBSCRIPTION_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].attributes").value(hasItem(DEFAULT_ATTRIBUTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Race.class);
        Race race1 = new Race();
        race1.setId(1L);
        Race race2 = new Race();
        race2.setId(race1.getId());
        assertThat(race1).isEqualTo(race2);
        race2.setId(2L);
        assertThat(race1).isNotEqualTo(race2);
        race1.setId(null);
        assertThat(race1).isNotEqualTo(race2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceDTO.class);
        RaceDTO raceDTO1 = new RaceDTO();
        raceDTO1.setId(1L);
        RaceDTO raceDTO2 = new RaceDTO();
        assertThat(raceDTO1).isNotEqualTo(raceDTO2);
        raceDTO2.setId(raceDTO1.getId());
        assertThat(raceDTO1).isEqualTo(raceDTO2);
        raceDTO2.setId(2L);
        assertThat(raceDTO1).isNotEqualTo(raceDTO2);
        raceDTO1.setId(null);
        assertThat(raceDTO1).isNotEqualTo(raceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(raceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(raceMapper.fromId(null)).isNull();
    }
}
