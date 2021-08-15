package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Discount;
import com.ushkov.dto.DiscountDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.DiscountRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class DiscountMapper {

    @Autowired
    protected DiscountRepositorySD DiscountRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    public abstract Discount map(DiscountDTO dto);

    public abstract DiscountDTO map(Discount entity);

    public Discount map(Short id){
        return DiscountRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Discount.class.getSimpleName()));
    }
}
