package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.PassengerClass;
import com.ushkov.dto.PassengerClassDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerClassRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class PassengerClassMapper {

    @Autowired
    protected PassengerClassRepositorySD passengerClassRepositorySD;

    @Mapping(target = "passengers", ignore = true)
    public abstract PassengerClass map(PassengerClassDTO dto);

    public abstract PassengerClassDTO map(PassengerClass entity);

    public PassengerClass mapFromId(Short id){
        return passengerClassRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, PassengerClass.class.getSimpleName()));
    }
}
