package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Plane;
import com.ushkov.dto.PlaneDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PlaneRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class PlaneMapper {

    @Autowired
    protected PlaneRepositorySD planeRepositorySD;

    @Mapping(target = "planeSeats", ignore = true)
    @Mapping(target = "flightPlanes", ignore = true)
    @Mapping(target = "airlines", ignore = true)
    public abstract Plane map(PlaneDTO dto);

    public abstract PlaneDTO map(Plane entity);

    public Plane mapFromId(Integer id){
        return planeRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Plane.class.getSimpleName()));
    }
}
