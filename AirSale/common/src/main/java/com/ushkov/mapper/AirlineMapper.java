package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Airline;
import com.ushkov.dto.AirlineDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirlineRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class AirlineMapper {
    @Autowired
    protected AirlineRepositorySD airlineRepositorySD;

    @Mapping(target = "flights", ignore = true)
    @Mapping(target = "planes", ignore = true)
    public abstract Airline map(AirlineDTO dto);

    public abstract AirlineDTO map(Airline entity);

    public Airline map(Short id){
        return airlineRepositorySD
                .findById(id)
                .orElseThrow(()-> new NoSuchEntityException(id, Airline.class.getSimpleName()));
    }

}
