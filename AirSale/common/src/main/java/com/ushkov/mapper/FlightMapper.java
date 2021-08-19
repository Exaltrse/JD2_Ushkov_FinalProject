package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Flight;
import com.ushkov.dto.FlightDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.FlightRepositorySD;

@Component
@Mapper(
        componentModel = "spring",
        uses = {
                AirlineMapper.class,
                AirportMapper.class
        })
public abstract class FlightMapper {

    @Autowired
    protected FlightRepositorySD FlightRepositorySD;

    @Mapping(target = "flightPlanes", ignore = true)
    @Mapping(target = "currentFlights", ignore = true)
    public abstract Flight map(FlightDTO dto);

    @Mapping(target = "airline", source = "airline.id")
    @Mapping(target = "departure", source = "departure.id")
    @Mapping(target = "destination", source = "destination.id")
    public abstract FlightDTO map(Flight entity);

    public Flight mapFromId(Integer id){
        return FlightRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Flight.class.getSimpleName()));
    }
}
