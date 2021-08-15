package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.CurrentFlightStatus;
import com.ushkov.dto.CurrentFlightStatusDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CurrentFlightStatusRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class CurrentFlightStatusMapper {
    @Autowired
    protected CurrentFlightStatusRepositorySD currentFlightStatusRepositorySD;


    @Mapping(target = "currentFlights", ignore = true)
    public abstract CurrentFlightStatus map(CurrentFlightStatusDTO dto);

    public abstract CurrentFlightStatusDTO map(CurrentFlightStatus entity);

    public CurrentFlightStatus map(Short id){
        return currentFlightStatusRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, CurrentFlightStatus.class.getSimpleName()));
    }
}
