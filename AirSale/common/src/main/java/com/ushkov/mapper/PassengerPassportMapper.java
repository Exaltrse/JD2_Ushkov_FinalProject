package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.PassengerPassport;
import com.ushkov.dto.PassengerPassportDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerPassportRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class PassengerPassportMapper {

    @Autowired
    protected PassengerPassportRepositorySD passengerPassportRepositorySD;

    public abstract PassengerPassport map(PassengerPassportDTO dto);

    public abstract PassengerPassportDTO map(PassengerPassport entity);

    public PassengerPassport mapFromId(Long id){
        return passengerPassportRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, PassengerPassport.class.getSimpleName()));
    }
}
