package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Passenger;
import com.ushkov.dto.PassengerDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerRepositorySD;

@Component
@Mapper(componentModel = "spring", uses = {PassengerClassMapper.class})
public abstract class PassengerMapper {

    @Autowired
    protected PassengerRepositorySD passengerRepositorySD;


    @Mapping(target = "users", ignore = true)
    @Mapping(target = "passports", ignore = true)
    public abstract Passenger map(PassengerDTO dto);

    @Mapping(target = "passengerClass", source = "passengerClass.id")
    public abstract PassengerDTO map(Passenger entity);

    public Passenger map(Long id){
        return passengerRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Passenger.class.getSimpleName()));
    }
}
