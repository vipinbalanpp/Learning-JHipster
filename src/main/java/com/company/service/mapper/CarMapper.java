package com.company.service.mapper;

import com.company.domain.Car;
import com.company.domain.Owner;
import com.company.service.dto.CarDTO;
import com.company.service.dto.OwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "ownerId")
    CarDTO toDto(Car s);

    @Named("ownerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OwnerDTO toDtoOwnerId(Owner owner);
}
