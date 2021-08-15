package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.UserPassenger;
import com.ushkov.dto.UserPassengerDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.UserPassengerRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class UserPassengerMapper {

    @Autowired
    protected UserPassengerRepositorySD userPassengerRepositorySD;

    public abstract UserPassenger map(UserPassengerDTO dto);

    public abstract UserPassengerDTO map(UserPassenger entity);

    public UserPassenger map(Long id){
        return userPassengerRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, UserPassenger.class.getSimpleName()));
    }
}