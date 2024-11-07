package com.company.service.mapper;

import static com.company.domain.CarAsserts.*;
import static com.company.domain.CarTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    void setUp() {
        carMapper = new CarMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarSample1();
        var actual = carMapper.toEntity(carMapper.toDto(expected));
        assertCarAllPropertiesEquals(expected, actual);
    }
}
