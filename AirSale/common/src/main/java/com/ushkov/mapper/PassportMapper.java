package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Passport;
import com.ushkov.dto.PassportDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassportRepositorySD;

@Component
@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public abstract class PassportMapper {

    @Autowired
    protected PassportRepositorySD passportRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "passengers", ignore = true)
    public abstract Passport map(PassportDTO dto);

    @Mapping(target = "citizenship", source = "citizenship.id")
    public abstract PassportDTO map(Passport entity);

    public Passport map(Long id){
        return passportRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Passport.class.getSimpleName()));
    }
}
