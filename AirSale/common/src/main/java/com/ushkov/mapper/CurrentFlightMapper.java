package com.ushkov.mapper;

import java.util.TimeZone;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.dto.CurrentFlightDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;

@Component
@Mapper(
        componentModel = "spring",
        uses = {
                FlightMapper.class,
                CurrentFlightStatusMapper.class,
                FlightPlaneMapper.class,
                TimestampMapper.class
        })
public abstract class CurrentFlightMapper {
    @Autowired
    protected CurrentFlightRepositorySD currentFlightRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "departureDate", source = "departureDate")
    @Mapping(target = "arrivalDate", source = "arrivalDate")
    public abstract CurrentFlight mapTo(CurrentFlightDTO dto);

    @Mapping(target = "flight", source = "flight.id")
    @Mapping(target = "currentFlightsStatus", source = "currentFlightsStatus.id")
    @Mapping(target = "flightPlane", source = "flightPlane.id")
    @Mapping(target = "departureDate", source = "departureDate")
    @Mapping(target = "arrivalDate", source = "arrivalDate")
    public abstract CurrentFlightDTO mapFrom(CurrentFlight entity, @Context TimeZone zone);

    public CurrentFlight mapFromId(Long id){
        return currentFlightRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, CurrentFlight.class.getSimpleName()));
    }

}
