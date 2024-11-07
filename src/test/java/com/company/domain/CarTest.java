package com.company.domain;

import static com.company.domain.CarTestSamples.*;
import static com.company.domain.OwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void ownerTest() {
        Car car = getCarRandomSampleGenerator();
        Owner ownerBack = getOwnerRandomSampleGenerator();

        car.setOwner(ownerBack);
        assertThat(car.getOwner()).isEqualTo(ownerBack);

        car.owner(null);
        assertThat(car.getOwner()).isNull();
    }
}
