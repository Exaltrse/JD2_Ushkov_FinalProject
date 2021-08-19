package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Country;
import com.ushkov.dto.CountryDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CountryRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class CountryMapper {
    @Autowired
    protected CountryRepositorySD countryRepositorySD;

    @Mapping(target = "passports", ignore = true)
    public abstract Country map(CountryDTO dto);

    public abstract CountryDTO map(Country entity);

    public Country mapFromId(Short id){
        return countryRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Country.class.getSimpleName()));
    }
}
