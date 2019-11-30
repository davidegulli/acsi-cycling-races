package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.SubscriptionDiscount;
import it.acsi.cycling.races.repository.SubscriptionDiscountRepository;
import it.acsi.cycling.races.repository.search.SubscriptionDiscountSearchRepository;
import it.acsi.cycling.races.service.SubscriptionDiscountService;
import it.acsi.cycling.races.service.dto.SubscriptionDiscountDTO;
import it.acsi.cycling.races.service.mapper.SubscriptionDiscountMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static it.acsi.cycling.races.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.acsi.cycling.races.domain.enumeration.DiscountType;
/**
 * Integration tests for the {@link SubscriptionDiscountResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class SubscriptionDiscountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final DiscountType DEFAULT_TYPE = DiscountType.PERCENT;
    private static final DiscountType UPDATED_TYPE = DiscountType.AMOUNT;

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private SubscriptionDiscountRepository subscriptionDiscountRepository;

    @Autowired
    private SubscriptionDiscountMapper subscriptionDiscountMapper;

    @Autowired
    private SubscriptionDiscountService subscriptionDiscountService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.SubscriptionDiscountSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubscriptionDiscountSearchRepository mockSubscriptionDiscountSearchRepository;

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

    private MockMvc restSubscriptionDiscountMockMvc;

    private SubscriptionDiscount subscriptionDiscount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriptionDiscountResource subscriptionDiscountResource = new SubscriptionDiscountResource(subscriptionDiscountService);
        this.restSubscriptionDiscountMockMvc = MockMvcBuilders.standaloneSetup(subscriptionDiscountResource)
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
    public static SubscriptionDiscount createEntity(EntityManager em) {
        SubscriptionDiscount subscriptionDiscount = new SubscriptionDiscount()
            .name(DEFAULT_NAME)
            .discount(DEFAULT_DISCOUNT)
            .type(DEFAULT_TYPE)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
        return subscriptionDiscount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDiscount createUpdatedEntity(EntityManager em) {
        SubscriptionDiscount subscriptionDiscount = new SubscriptionDiscount()
            .name(UPDATED_NAME)
            .discount(UPDATED_DISCOUNT)
            .type(UPDATED_TYPE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        return subscriptionDiscount;
    }

    @BeforeEach
    public void initTest() {
        subscriptionDiscount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionDiscount() throws Exception {
        int databaseSizeBeforeCreate = subscriptionDiscountRepository.findAll().size();

        // Create the SubscriptionDiscount
        SubscriptionDiscountDTO subscriptionDiscountDTO = subscriptionDiscountMapper.toDto(subscriptionDiscount);
        restSubscriptionDiscountMockMvc.perform(post("/api/subscription-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionDiscountDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionDiscount in the database
        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionDiscount testSubscriptionDiscount = subscriptionDiscountList.get(subscriptionDiscountList.size() - 1);
        assertThat(testSubscriptionDiscount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubscriptionDiscount.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testSubscriptionDiscount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSubscriptionDiscount.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);

        // Validate the SubscriptionDiscount in Elasticsearch
        verify(mockSubscriptionDiscountSearchRepository, times(1)).save(testSubscriptionDiscount);
    }

    @Test
    @Transactional
    public void createSubscriptionDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionDiscountRepository.findAll().size();

        // Create the SubscriptionDiscount with an existing ID
        subscriptionDiscount.setId(1L);
        SubscriptionDiscountDTO subscriptionDiscountDTO = subscriptionDiscountMapper.toDto(subscriptionDiscount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionDiscountMockMvc.perform(post("/api/subscription-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDiscount in the database
        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeCreate);

        // Validate the SubscriptionDiscount in Elasticsearch
        verify(mockSubscriptionDiscountSearchRepository, times(0)).save(subscriptionDiscount);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionDiscountRepository.findAll().size();
        // set the field null
        subscriptionDiscount.setName(null);

        // Create the SubscriptionDiscount, which fails.
        SubscriptionDiscountDTO subscriptionDiscountDTO = subscriptionDiscountMapper.toDto(subscriptionDiscount);

        restSubscriptionDiscountMockMvc.perform(post("/api/subscription-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionDiscountDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionDiscounts() throws Exception {
        // Initialize the database
        subscriptionDiscountRepository.saveAndFlush(subscriptionDiscount);

        // Get all the subscriptionDiscountList
        restSubscriptionDiscountMockMvc.perform(get("/api/subscription-discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionDiscount() throws Exception {
        // Initialize the database
        subscriptionDiscountRepository.saveAndFlush(subscriptionDiscount);

        // Get the subscriptionDiscount
        restSubscriptionDiscountMockMvc.perform(get("/api/subscription-discounts/{id}", subscriptionDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionDiscount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubscriptionDiscount() throws Exception {
        // Get the subscriptionDiscount
        restSubscriptionDiscountMockMvc.perform(get("/api/subscription-discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionDiscount() throws Exception {
        // Initialize the database
        subscriptionDiscountRepository.saveAndFlush(subscriptionDiscount);

        int databaseSizeBeforeUpdate = subscriptionDiscountRepository.findAll().size();

        // Update the subscriptionDiscount
        SubscriptionDiscount updatedSubscriptionDiscount = subscriptionDiscountRepository.findById(subscriptionDiscount.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionDiscount are not directly saved in db
        em.detach(updatedSubscriptionDiscount);
        updatedSubscriptionDiscount
            .name(UPDATED_NAME)
            .discount(UPDATED_DISCOUNT)
            .type(UPDATED_TYPE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        SubscriptionDiscountDTO subscriptionDiscountDTO = subscriptionDiscountMapper.toDto(updatedSubscriptionDiscount);

        restSubscriptionDiscountMockMvc.perform(put("/api/subscription-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionDiscountDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionDiscount in the database
        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionDiscount testSubscriptionDiscount = subscriptionDiscountList.get(subscriptionDiscountList.size() - 1);
        assertThat(testSubscriptionDiscount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubscriptionDiscount.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testSubscriptionDiscount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSubscriptionDiscount.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);

        // Validate the SubscriptionDiscount in Elasticsearch
        verify(mockSubscriptionDiscountSearchRepository, times(1)).save(testSubscriptionDiscount);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionDiscount() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionDiscountRepository.findAll().size();

        // Create the SubscriptionDiscount
        SubscriptionDiscountDTO subscriptionDiscountDTO = subscriptionDiscountMapper.toDto(subscriptionDiscount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDiscountMockMvc.perform(put("/api/subscription-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDiscount in the database
        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubscriptionDiscount in Elasticsearch
        verify(mockSubscriptionDiscountSearchRepository, times(0)).save(subscriptionDiscount);
    }

    @Test
    @Transactional
    public void deleteSubscriptionDiscount() throws Exception {
        // Initialize the database
        subscriptionDiscountRepository.saveAndFlush(subscriptionDiscount);

        int databaseSizeBeforeDelete = subscriptionDiscountRepository.findAll().size();

        // Delete the subscriptionDiscount
        restSubscriptionDiscountMockMvc.perform(delete("/api/subscription-discounts/{id}", subscriptionDiscount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionDiscount> subscriptionDiscountList = subscriptionDiscountRepository.findAll();
        assertThat(subscriptionDiscountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SubscriptionDiscount in Elasticsearch
        verify(mockSubscriptionDiscountSearchRepository, times(1)).deleteById(subscriptionDiscount.getId());
    }

    @Test
    @Transactional
    public void searchSubscriptionDiscount() throws Exception {
        // Initialize the database
        subscriptionDiscountRepository.saveAndFlush(subscriptionDiscount);
        when(mockSubscriptionDiscountSearchRepository.search(queryStringQuery("id:" + subscriptionDiscount.getId())))
            .thenReturn(Collections.singletonList(subscriptionDiscount));
        // Search the subscriptionDiscount
        restSubscriptionDiscountMockMvc.perform(get("/api/_search/subscription-discounts?query=id:" + subscriptionDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDiscount.class);
        SubscriptionDiscount subscriptionDiscount1 = new SubscriptionDiscount();
        subscriptionDiscount1.setId(1L);
        SubscriptionDiscount subscriptionDiscount2 = new SubscriptionDiscount();
        subscriptionDiscount2.setId(subscriptionDiscount1.getId());
        assertThat(subscriptionDiscount1).isEqualTo(subscriptionDiscount2);
        subscriptionDiscount2.setId(2L);
        assertThat(subscriptionDiscount1).isNotEqualTo(subscriptionDiscount2);
        subscriptionDiscount1.setId(null);
        assertThat(subscriptionDiscount1).isNotEqualTo(subscriptionDiscount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDiscountDTO.class);
        SubscriptionDiscountDTO subscriptionDiscountDTO1 = new SubscriptionDiscountDTO();
        subscriptionDiscountDTO1.setId(1L);
        SubscriptionDiscountDTO subscriptionDiscountDTO2 = new SubscriptionDiscountDTO();
        assertThat(subscriptionDiscountDTO1).isNotEqualTo(subscriptionDiscountDTO2);
        subscriptionDiscountDTO2.setId(subscriptionDiscountDTO1.getId());
        assertThat(subscriptionDiscountDTO1).isEqualTo(subscriptionDiscountDTO2);
        subscriptionDiscountDTO2.setId(2L);
        assertThat(subscriptionDiscountDTO1).isNotEqualTo(subscriptionDiscountDTO2);
        subscriptionDiscountDTO1.setId(null);
        assertThat(subscriptionDiscountDTO1).isNotEqualTo(subscriptionDiscountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subscriptionDiscountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subscriptionDiscountMapper.fromId(null)).isNull();
    }
}
