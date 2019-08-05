package it.acsi.cycling.races.web.rest;

import it.acsi.cycling.races.AcsiCyclingRacesApp;
import it.acsi.cycling.races.domain.File;
import it.acsi.cycling.races.repository.FileRepository;
import it.acsi.cycling.races.repository.search.FileSearchRepository;
import it.acsi.cycling.races.service.FileService;
import it.acsi.cycling.races.service.dto.FileDTO;
import it.acsi.cycling.races.service.mapper.FileMapper;
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
import org.springframework.util.Base64Utils;
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

import it.acsi.cycling.races.domain.enumeration.FileType;
/**
 * Integration tests for the {@Link FileResource} REST controller.
 */
@SpringBootTest(classes = AcsiCyclingRacesApp.class)
public class FileResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final FileType DEFAULT_TYPE = FileType.COVER_IMAGE;
    private static final FileType UPDATED_TYPE = FileType.LOGO_IMAGE;

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BINARY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BINARY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BINARY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BINARY_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileService fileService;

    /**
     * This repository is mocked in the it.acsi.cycling.races.repository.search test package.
     *
     * @see it.acsi.cycling.races.repository.search.FileSearchRepositoryMockConfiguration
     */
    @Autowired
    private FileSearchRepository mockFileSearchRepository;

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

    private MockMvc restFileMockMvc;

    private File file;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileResource fileResource = new FileResource(fileService);
        this.restFileMockMvc = MockMvcBuilders.standaloneSetup(fileResource)
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
    public static File createEntity(EntityManager em) {
        File file = new File()
            .title(DEFAULT_TITLE)
            .type(DEFAULT_TYPE)
            .mimeType(DEFAULT_MIME_TYPE)
            .binary(DEFAULT_BINARY)
            .binaryContentType(DEFAULT_BINARY_CONTENT_TYPE)
            .url(DEFAULT_URL);
        return file;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File()
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .binary(UPDATED_BINARY)
            .binaryContentType(UPDATED_BINARY_CONTENT_TYPE)
            .url(UPDATED_URL);
        return file;
    }

    @BeforeEach
    public void initTest() {
        file = createEntity(em);
    }

    @Test
    @Transactional
    public void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);
        restFileMockMvc.perform(post("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isCreated());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFile.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testFile.getBinary()).isEqualTo(DEFAULT_BINARY);
        assertThat(testFile.getBinaryContentType()).isEqualTo(DEFAULT_BINARY_CONTENT_TYPE);
        assertThat(testFile.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the File in Elasticsearch
        verify(mockFileSearchRepository, times(1)).save(testFile);
    }

    @Test
    @Transactional
    public void createFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // Create the File with an existing ID
        file.setId(1L);
        FileDTO fileDTO = fileMapper.toDto(file);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc.perform(post("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate);

        // Validate the File in Elasticsearch
        verify(mockFileSearchRepository, times(0)).save(file);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileRepository.findAll().size();
        // set the field null
        file.setType(null);

        // Create the File, which fails.
        FileDTO fileDTO = fileMapper.toDto(file);

        restFileMockMvc.perform(post("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileRepository.findAll().size();
        // set the field null
        file.setMimeType(null);

        // Create the File, which fails.
        FileDTO fileDTO = fileMapper.toDto(file);

        restFileMockMvc.perform(post("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc.perform(get("/api/files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].binaryContentType").value(hasItem(DEFAULT_BINARY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].binary").value(hasItem(Base64Utils.encodeToString(DEFAULT_BINARY))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc.perform(get("/api/files/{id}", file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.binaryContentType").value(DEFAULT_BINARY_CONTENT_TYPE))
            .andExpect(jsonPath("$.binary").value(Base64Utils.encodeToString(DEFAULT_BINARY)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get("/api/files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).get();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .binary(UPDATED_BINARY)
            .binaryContentType(UPDATED_BINARY_CONTENT_TYPE)
            .url(UPDATED_URL);
        FileDTO fileDTO = fileMapper.toDto(updatedFile);

        restFileMockMvc.perform(put("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFile.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testFile.getBinary()).isEqualTo(UPDATED_BINARY);
        assertThat(testFile.getBinaryContentType()).isEqualTo(UPDATED_BINARY_CONTENT_TYPE);
        assertThat(testFile.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the File in Elasticsearch
        verify(mockFileSearchRepository, times(1)).save(testFile);
    }

    @Test
    @Transactional
    public void updateNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(put("/api/files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the File in Elasticsearch
        verify(mockFileSearchRepository, times(0)).save(file);
    }

    @Test
    @Transactional
    public void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeDelete = fileRepository.findAll().size();

        // Delete the file
        restFileMockMvc.perform(delete("/api/files/{id}", file.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the File in Elasticsearch
        verify(mockFileSearchRepository, times(1)).deleteById(file.getId());
    }

    @Test
    @Transactional
    public void searchFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);
        when(mockFileSearchRepository.search(queryStringQuery("id:" + file.getId())))
            .thenReturn(Collections.singletonList(file));
        // Search the file
        restFileMockMvc.perform(get("/api/_search/files?query=id:" + file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].binaryContentType").value(hasItem(DEFAULT_BINARY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].binary").value(hasItem(Base64Utils.encodeToString(DEFAULT_BINARY))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(File.class);
        File file1 = new File();
        file1.setId(1L);
        File file2 = new File();
        file2.setId(file1.getId());
        assertThat(file1).isEqualTo(file2);
        file2.setId(2L);
        assertThat(file1).isNotEqualTo(file2);
        file1.setId(null);
        assertThat(file1).isNotEqualTo(file2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDTO.class);
        FileDTO fileDTO1 = new FileDTO();
        fileDTO1.setId(1L);
        FileDTO fileDTO2 = new FileDTO();
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
        fileDTO2.setId(fileDTO1.getId());
        assertThat(fileDTO1).isEqualTo(fileDTO2);
        fileDTO2.setId(2L);
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
        fileDTO1.setId(null);
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fileMapper.fromId(null)).isNull();
    }
}
