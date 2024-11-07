package com.company.service;

import com.company.domain.Car;
import com.company.repository.CarRepository;
import com.company.service.dto.CarDTO;
import com.company.service.mapper.CarMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.company.domain.Car}.
 */
@Service
@Transactional
public class CarService {

    private static final Logger LOG = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    public CarDTO save(CarDTO carDTO) {
        LOG.debug("Request to save Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    /**
     * Update a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    public CarDTO update(CarDTO carDTO) {
        LOG.debug("Request to update Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    /**
     * Partially update a car.
     *
     * @param carDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CarDTO> partialUpdate(CarDTO carDTO) {
        LOG.debug("Request to partially update Car : {}", carDTO);

        return carRepository
            .findById(carDTO.getId())
            .map(existingCar -> {
                carMapper.partialUpdate(existingCar, carDTO);

                return existingCar;
            })
            .map(carRepository::save)
            .map(carMapper::toDto);
    }

    /**
     * Get all the cars.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CarDTO> findAll() {
        LOG.debug("Request to get all Cars");
        return carRepository.findAll().stream().map(carMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarDTO> findOne(Long id) {
        LOG.debug("Request to get Car : {}", id);
        return carRepository.findById(id).map(carMapper::toDto);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
