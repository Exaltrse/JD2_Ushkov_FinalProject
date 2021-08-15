package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.SeatClass;
import com.ushkov.dto.SeatClassDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.SeatClassRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class SeatClassMapper {

    @Autowired
    protected SeatClassRepositorySD seatClassRepositorySD;

    @Mapping(target = "planeSeats", ignore = true)
    public abstract SeatClass map(SeatClassDTO dto);

    public abstract SeatClassDTO map(SeatClass entity);

    public SeatClass map(Short id){
        return seatClassRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, SeatClass.class.getSimpleName()));
    }
}
