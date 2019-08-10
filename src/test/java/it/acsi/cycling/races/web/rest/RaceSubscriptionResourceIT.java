package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.RaceSubscription;
import it.acsi.cycling.races.repository.RaceSubscriptionRepository;
import it.acsi.cycling.races.repository.search.RaceSubscriptionSearchRepository;
import it.acsi.cycling.races.service.RaceSubscriptionService;
import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;
import it.acsi.cycling.races.service.mapper.RaceSubscriptionMapper;
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

import it.acsi.cycling.races.domain.enumeration.GenderType;
import it.acsi.cycling.races.domain.enumeration.PaymentType;
/**
 * Integration tests for the {@link RaceSubscriptionResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class RaceSubscriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_DATE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final String DEFAULT_TAX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Long DEFAULT_SUBCRIPTION_TYPE_ID = 1L;
    private static final Long UPDATED_SUBCRIPTION_TYPE_ID = 2L;
    private static final Long SMALLER_SUBCRIPTION_TYPE_ID = 1L - 1L;

    private static final Long DEFAULT_PATH_TYPE = 1L;
    private static final Long UPDATED_PATH_TYPE = 2L;
    private static final Long SMALLER_PATH_TYPE = 1L - 1L;

    private static final Long DEFAULT_TEAM_ID = 1L;
    private static final Long UPDATED_TEAM_ID = 2L;
    private static final Long SMALLER_TEAM_ID = 1L - 1L;

    private static final String DEFAULT_ATHLETE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ATHLETE_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE = Instant.ofEpochMilli(-1L);

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.PAYPAL;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.CREDIT_TRANSFER;

    private static final Boolean DEFAULT_PAYED = false;
    private static final Boolean UPDATED_PAYED = true;

    private static final Double DEFAULT_PAYED_PRICE = 1D;
    private static final Double UPDATED_PAYED_PRICE = 2D;
    private static final Double SMALLER_PAYED_PRICE = 1D - 1D;

    @Autowired
    private RaceSubscriptionRepository raceSubscriptionRepository;

    @Autowired
    private RaceSubscriptionMapper raceSubscriptionMapper;

    @Autowired
    private RaceSubscriptionService raceSubscriptionService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.RaceSubscriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private RaceSubscriptionSearchRepository mockRaceSubscriptionSearchRepository;

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

    private MockMvc restRaceSubscriptionMockMvc;

    private RaceSubscription raceSubscription;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RaceSubscriptionResource raceSubscriptionResource = new RaceSubscriptionResource(raceSubscriptionService);
        this.restRaceSubscriptionMockMvc = MockMvcBuilders.standaloneSetup(raceSubscriptionResource)
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
    public static RaceSubscription createEntity(EntityManager em) {
        RaceSubscription raceSubscription = new RaceSubscription()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .gender(DEFAULT_GENDER)
            .taxCode(DEFAULT_TAX_CODE)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .category(DEFAULT_CATEGORY)
            .subcriptionTypeId(DEFAULT_SUBCRIPTION_TYPE_ID)
            .pathType(DEFAULT_PATH_TYPE)
            .teamId(DEFAULT_TEAM_ID)
            .athleteId(DEFAULT_ATHLETE_ID)
            .date(DEFAULT_DATE)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .payed(DEFAULT_PAYED)
            .payedPrice(DEFAULT_PAYED_PRICE);
        return raceSubscription;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaceSubscription createUpdatedEntity(EntityManager em) {
        RaceSubscription raceSubscription = new RaceSubscription()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .gender(UPDATED_GENDER)
            .taxCode(UPDATED_TAX_CODE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .category(UPDATED_CATEGORY)
            .subcriptionTypeId(UPDATED_SUBCRIPTION_TYPE_ID)
            .pathType(UPDATED_PATH_TYPE)
            .teamId(UPDATED_TEAM_ID)
            .athleteId(UPDATED_ATHLETE_ID)
            .date(UPDATED_DATE)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .payed(UPDATED_PAYED)
            .payedPrice(UPDATED_PAYED_PRICE);
        return raceSubscription;
    }

    @BeforeEach
    public void initTest() {
        raceSubscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createRaceSubscription() throws Exception {
        int databaseSizeBeforeCreate = raceSubscriptionRepository.findAll().size();

        // Create the RaceSubscription
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);
        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the RaceSubscription in the database
        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        RaceSubscription testRaceSubscription = raceSubscriptionList.get(raceSubscriptionList.size() - 1);
        assertThat(testRaceSubscription.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRaceSubscription.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testRaceSubscription.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testRaceSubscription.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testRaceSubscription.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testRaceSubscription.getTaxCode()).isEqualTo(DEFAULT_TAX_CODE);
        assertThat(testRaceSubscription.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRaceSubscription.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRaceSubscription.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testRaceSubscription.getSubcriptionTypeId()).isEqualTo(DEFAULT_SUBCRIPTION_TYPE_ID);
        assertThat(testRaceSubscription.getPathType()).isEqualTo(DEFAULT_PATH_TYPE);
        assertThat(testRaceSubscription.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testRaceSubscription.getAthleteId()).isEqualTo(DEFAULT_ATHLETE_ID);
        assertThat(testRaceSubscription.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRaceSubscription.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testRaceSubscription.isPayed()).isEqualTo(DEFAULT_PAYED);
        assertThat(testRaceSubscription.getPayedPrice()).isEqualTo(DEFAULT_PAYED_PRICE);

        // Validate the RaceSubscription in Elasticsearch
        verify(mockRaceSubscriptionSearchRepository, times(1)).save(testRaceSubscription);
    }

    @Test
    @Transactional
    public void createRaceSubscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raceSubscriptionRepository.findAll().size();

        // Create the RaceSubscription with an existing ID
        raceSubscription.setId(1L);
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaceSubscription in the database
        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the RaceSubscription in Elasticsearch
        verify(mockRaceSubscriptionSearchRepository, times(0)).save(raceSubscription);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setName(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setSurname(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setBirthDate(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setBirthPlace(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setGender(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setTaxCode(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setEmail(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setPhone(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubcriptionTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setSubcriptionTypeId(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setPathType(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTeamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setTeamId(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAthleteIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setAthleteId(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setDate(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceSubscriptionRepository.findAll().size();
        // set the field null
        raceSubscription.setPaymentType(null);

        // Create the RaceSubscription, which fails.
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        restRaceSubscriptionMockMvc.perform(post("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRaceSubscriptions() throws Exception {
        // Initialize the database
        raceSubscriptionRepository.saveAndFlush(raceSubscription);

        // Get all the raceSubscriptionList
        restRaceSubscriptionMockMvc.perform(get("/api/race-subscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raceSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].subcriptionTypeId").value(hasItem(DEFAULT_SUBCRIPTION_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].pathType").value(hasItem(DEFAULT_PATH_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].athleteId").value(hasItem(DEFAULT_ATHLETE_ID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].payed").value(hasItem(DEFAULT_PAYED.booleanValue())))
            .andExpect(jsonPath("$.[*].payedPrice").value(hasItem(DEFAULT_PAYED_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRaceSubscription() throws Exception {
        // Initialize the database
        raceSubscriptionRepository.saveAndFlush(raceSubscription);

        // Get the raceSubscription
        restRaceSubscriptionMockMvc.perform(get("/api/race-subscriptions/{id}", raceSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(raceSubscription.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.taxCode").value(DEFAULT_TAX_CODE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.subcriptionTypeId").value(DEFAULT_SUBCRIPTION_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.pathType").value(DEFAULT_PATH_TYPE.intValue()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.intValue()))
            .andExpect(jsonPath("$.athleteId").value(DEFAULT_ATHLETE_ID.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.payed").value(DEFAULT_PAYED.booleanValue()))
            .andExpect(jsonPath("$.payedPrice").value(DEFAULT_PAYED_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRaceSubscription() throws Exception {
        // Get the raceSubscription
        restRaceSubscriptionMockMvc.perform(get("/api/race-subscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRaceSubscription() throws Exception {
        // Initialize the database
        raceSubscriptionRepository.saveAndFlush(raceSubscription);

        int databaseSizeBeforeUpdate = raceSubscriptionRepository.findAll().size();

        // Update the raceSubscription
        RaceSubscription updatedRaceSubscription = raceSubscriptionRepository.findById(raceSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedRaceSubscription are not directly saved in db
        em.detach(updatedRaceSubscription);
        updatedRaceSubscription
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .gender(UPDATED_GENDER)
            .taxCode(UPDATED_TAX_CODE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .category(UPDATED_CATEGORY)
            .subcriptionTypeId(UPDATED_SUBCRIPTION_TYPE_ID)
            .pathType(UPDATED_PATH_TYPE)
            .teamId(UPDATED_TEAM_ID)
            .athleteId(UPDATED_ATHLETE_ID)
            .date(UPDATED_DATE)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .payed(UPDATED_PAYED)
            .payedPrice(UPDATED_PAYED_PRICE);
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(updatedRaceSubscription);

        restRaceSubscriptionMockMvc.perform(put("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isOk());

        // Validate the RaceSubscription in the database
        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        RaceSubscription testRaceSubscription = raceSubscriptionList.get(raceSubscriptionList.size() - 1);
        assertThat(testRaceSubscription.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRaceSubscription.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testRaceSubscription.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testRaceSubscription.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testRaceSubscription.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testRaceSubscription.getTaxCode()).isEqualTo(UPDATED_TAX_CODE);
        assertThat(testRaceSubscription.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRaceSubscription.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRaceSubscription.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testRaceSubscription.getSubcriptionTypeId()).isEqualTo(UPDATED_SUBCRIPTION_TYPE_ID);
        assertThat(testRaceSubscription.getPathType()).isEqualTo(UPDATED_PATH_TYPE);
        assertThat(testRaceSubscription.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testRaceSubscription.getAthleteId()).isEqualTo(UPDATED_ATHLETE_ID);
        assertThat(testRaceSubscription.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRaceSubscription.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testRaceSubscription.isPayed()).isEqualTo(UPDATED_PAYED);
        assertThat(testRaceSubscription.getPayedPrice()).isEqualTo(UPDATED_PAYED_PRICE);

        // Validate the RaceSubscription in Elasticsearch
        verify(mockRaceSubscriptionSearchRepository, times(1)).save(testRaceSubscription);
    }

    @Test
    @Transactional
    public void updateNonExistingRaceSubscription() throws Exception {
        int databaseSizeBeforeUpdate = raceSubscriptionRepository.findAll().size();

        // Create the RaceSubscription
        RaceSubscriptionDTO raceSubscriptionDTO = raceSubscriptionMapper.toDto(raceSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceSubscriptionMockMvc.perform(put("/api/race-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(raceSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaceSubscription in the database
        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RaceSubscription in Elasticsearch
        verify(mockRaceSubscriptionSearchRepository, times(0)).save(raceSubscription);
    }

    @Test
    @Transactional
    public void deleteRaceSubscription() throws Exception {
        // Initialize the database
        raceSubscriptionRepository.saveAndFlush(raceSubscription);

        int databaseSizeBeforeDelete = raceSubscriptionRepository.findAll().size();

        // Delete the raceSubscription
        restRaceSubscriptionMockMvc.perform(delete("/api/race-subscriptions/{id}", raceSubscription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RaceSubscription> raceSubscriptionList = raceSubscriptionRepository.findAll();
        assertThat(raceSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RaceSubscription in Elasticsearch
        verify(mockRaceSubscriptionSearchRepository, times(1)).deleteById(raceSubscription.getId());
    }

    @Test
    @Transactional
    public void searchRaceSubscription() throws Exception {
        // Initialize the database
        raceSubscriptionRepository.saveAndFlush(raceSubscription);
        when(mockRaceSubscriptionSearchRepository.search(queryStringQuery("id:" + raceSubscription.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(raceSubscription), PageRequest.of(0, 1), 1));
        // Search the raceSubscription
        restRaceSubscriptionMockMvc.perform(get("/api/_search/race-subscriptions?query=id:" + raceSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raceSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].subcriptionTypeId").value(hasItem(DEFAULT_SUBCRIPTION_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].pathType").value(hasItem(DEFAULT_PATH_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].athleteId").value(hasItem(DEFAULT_ATHLETE_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].payed").value(hasItem(DEFAULT_PAYED.booleanValue())))
            .andExpect(jsonPath("$.[*].payedPrice").value(hasItem(DEFAULT_PAYED_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceSubscription.class);
        RaceSubscription raceSubscription1 = new RaceSubscription();
        raceSubscription1.setId(1L);
        RaceSubscription raceSubscription2 = new RaceSubscription();
        raceSubscription2.setId(raceSubscription1.getId());
        assertThat(raceSubscription1).isEqualTo(raceSubscription2);
        raceSubscription2.setId(2L);
        assertThat(raceSubscription1).isNotEqualTo(raceSubscription2);
        raceSubscription1.setId(null);
        assertThat(raceSubscription1).isNotEqualTo(raceSubscription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceSubscriptionDTO.class);
        RaceSubscriptionDTO raceSubscriptionDTO1 = new RaceSubscriptionDTO();
        raceSubscriptionDTO1.setId(1L);
        RaceSubscriptionDTO raceSubscriptionDTO2 = new RaceSubscriptionDTO();
        assertThat(raceSubscriptionDTO1).isNotEqualTo(raceSubscriptionDTO2);
        raceSubscriptionDTO2.setId(raceSubscriptionDTO1.getId());
        assertThat(raceSubscriptionDTO1).isEqualTo(raceSubscriptionDTO2);
        raceSubscriptionDTO2.setId(2L);
        assertThat(raceSubscriptionDTO1).isNotEqualTo(raceSubscriptionDTO2);
        raceSubscriptionDTO1.setId(null);
        assertThat(raceSubscriptionDTO1).isNotEqualTo(raceSubscriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(raceSubscriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(raceSubscriptionMapper.fromId(null)).isNull();
    }
}
