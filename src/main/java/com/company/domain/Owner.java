package com.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Owner.
 */
@Entity
@Table(name = "owner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Owner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Set<Car> cars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Owner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Owner name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public Owner gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Car> getCars() {
        return this.cars;
    }

    public void setCars(Set<Car> cars) {
        if (this.cars != null) {
            this.cars.forEach(i -> i.setOwner(null));
        }
        if (cars != null) {
            cars.forEach(i -> i.setOwner(this));
        }
        this.cars = cars;
    }

    public Owner cars(Set<Car> cars) {
        this.setCars(cars);
        return this;
    }

    public Owner addCar(Car car) {
        this.cars.add(car);
        car.setOwner(this);
        return this;
    }

    public Owner removeCar(Car car) {
        this.cars.remove(car);
        car.setOwner(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Owner)) {
            return false;
        }
        return getId() != null && getId().equals(((Owner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Owner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
