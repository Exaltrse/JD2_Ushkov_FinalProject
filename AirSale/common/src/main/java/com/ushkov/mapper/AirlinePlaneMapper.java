package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.AirlinePlane;
import com.ushkov.dto.AirlinePlaneDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirlinePlaneRepositorySD;

@Component
@Mapper(componentModel = "spring", uses = {AirlineMapper.class, PlaneMapper.class})
public abstract class AirlinePlaneMapper {
    @Autowired
    protected AirlinePlaneRepositorySD airlinePlaneRepositorySD;

    public abstract AirlinePlane map(AirlinePlaneDTO dto);

    public abstract AirlinePlaneDTO map(AirlinePlane entity);

    public AirlinePlane mapFromId(Long id){
        return airlinePlaneRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, AirlinePlane.class.getSimpleName()));
    }
}
