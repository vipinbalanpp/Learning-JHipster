package com.company.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.company.domain.Owner} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OwnerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OwnerDTO)) {
            return false;
        }

        OwnerDTO ownerDTO = (OwnerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ownerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
