package com.company.domain;

import static com.company.domain.CarTestSamples.*;
import static com.company.domain.OwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = getOwnerSample1();
        Owner owner2 = new Owner();
        assertThat(owner1).isNotEqualTo(owner2);

        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);

        owner2 = getOwnerSample2();
        assertThat(owner1).isNotEqualTo(owner2);
    }

    @Test
    void carTest() {
        Owner owner = getOwnerRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        owner.addCar(carBack);
        assertThat(owner.getCars()).containsOnly(carBack);
        assertThat(carBack.getOwner()).isEqualTo(owner);

        owner.removeCar(carBack);
        assertThat(owner.getCars()).doesNotContain(carBack);
        assertThat(carBack.getOwner()).isNull();

        owner.cars(new HashSet<>(Set.of(carBack)));
        assertThat(owner.getCars()).containsOnly(carBack);
        assertThat(carBack.getOwner()).isEqualTo(owner);

        owner.setCars(new HashSet<>());
        assertThat(owner.getCars()).doesNotContain(carBack);
        assertThat(carBack.getOwner()).isNull();
    }
}
