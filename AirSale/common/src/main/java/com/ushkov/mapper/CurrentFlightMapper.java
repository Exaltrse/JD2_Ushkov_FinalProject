package com.ushkov.mapper;

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
                FlightPlaneMapper.class})
public abstract class CurrentFlightMapper {
    @Autowired
    protected CurrentFlightRepositorySD currentFlightRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    public abstract CurrentFlight map(CurrentFlightDTO dto);

    @Mapping(target = "flight", source = "flight.id")
    @Mapping(target = "currentFlightsStatus", source = "currentFlightsStatus.id")
    @Mapping(target = "flightPlane", source = "flightPlane.id")
    public abstract CurrentFlightDTO map(CurrentFlight entity);

    public CurrentFlight map(Long id){
        return currentFlightRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, CurrentFlight.class.getSimpleName()));
    }
}
