package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Airport;
import com.ushkov.dto.AirportDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirportRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class AirportMapper {

    @Autowired
    protected AirportRepositorySD airportRepositorySD;

    @Mapping(target = "departure", ignore = true)
    @Mapping(target = "destination", ignore = true)
    public abstract Airport map(AirportDTO dto);

    public abstract AirportDTO map(Airport entity);

    public Airport map(Short id){
        return airportRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Airport.class.getSimpleName()));
    }
}
