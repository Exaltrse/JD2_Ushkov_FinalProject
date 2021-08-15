package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.PlaneSeats;
import com.ushkov.dto.PlaneSeatsDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PlaneSeatsRepositorySD;

@Component
@Mapper(componentModel = "spring", uses = {PlaneMapper.class, SeatClassMapper.class})
public abstract class PlaneSeatsMapper {

    @Autowired
    protected PlaneSeatsRepositorySD planeSeatsRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    public abstract PlaneSeats map(PlaneSeatsDTO dto);

    @Mapping(target = "plane", source = "plane.id")
    @Mapping(target = "seat", source = "seat.id")
    public abstract PlaneSeatsDTO map(PlaneSeats entity);

    public PlaneSeats map(Integer id){
        return planeSeatsRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, PlaneSeats.class.getSimpleName()));
    }
}
