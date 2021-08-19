package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.FlightPlane;
import com.ushkov.dto.FlightPlaneDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.FlightPlaneRepositorySD;

@Component
@Mapper(
        componentModel = "spring",
        uses = {
                FlightMapper.class,
                PlaneMapper.class
        })
public abstract class FlightPlaneMapper {

    @Autowired
    protected FlightPlaneRepositorySD flightPlaneRepositorySD;

    @Mapping(target = "currentFlights", ignore = true)
    public abstract FlightPlane map(FlightPlaneDTO dto);

    @Mapping(target = "flight", source = "flight.id")
    @Mapping(target = "plane", source = "plane.id")
    public abstract FlightPlaneDTO map(FlightPlane entity);

    public FlightPlane mapFromId(Long id){
        return flightPlaneRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, FlightPlane.class.getSimpleName()));
    }
}
