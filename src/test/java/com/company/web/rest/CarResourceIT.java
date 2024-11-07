package com.company.web.rest;

import static com.company.domain.CarAsserts.*;
import static com.company.web.rest.TestUtil.createUpdateProxyForBean;
import static com.company.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.IntegrationTest;
import com.company.domain.Car;
import com.company.repository.CarRepository;
import com.company.service.dto.CarDTO;
import com.company.service.mapper.CarMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    private Car insertedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity() {
        return new Car().name(DEFAULT_NAME).model(DEFAULT_MODEL).price(DEFAULT_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity() {
        return new Car().name(UPDATED_NAME).model(UPDATED_MODEL).price(UPDATED_PRICE);
    }

    @BeforeEach
    public void initTest() {
        car = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCar != null) {
            carRepository.delete(insertedCar);
            insertedCar = null;
        }
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        var returnedCarDTO = om.readValue(
            restCarMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarDTO.class
        );

        // Validate the Car in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCar = carMapper.toEntity(returnedCarDTO);
        assertCarUpdatableFieldsEquals(returnedCar, getPersistedCar(returnedCar));

        insertedCar = returnedCar;
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setName(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setModel(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setPrice(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar.name(UPDATED_NAME).model(UPDATED_MODEL).price(UPDATED_PRICE);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, carDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isOk());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarToMatchAllProperties(updatedCar);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, carDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCar, car), getPersistedCar(car));
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.name(UPDATED_NAME).model(UPDATED_MODEL).price(UPDATED_PRICE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(partialUpdatedCar, getPersistedCar(partialUpdatedCar));
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Car getPersistedCar(Car car) {
        return carRepository.findById(car.getId()).orElseThrow();
    }

    protected void assertPersistedCarToMatchAllProperties(Car expectedCar) {
        assertCarAllPropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }

    protected void assertPersistedCarToMatchUpdatableProperties(Car expectedCar) {
        assertCarAllUpdatablePropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }
}
