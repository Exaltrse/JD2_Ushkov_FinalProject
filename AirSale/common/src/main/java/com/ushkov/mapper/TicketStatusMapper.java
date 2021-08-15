package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.TicketStatus;
import com.ushkov.dto.TicketStatusDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.TicketStatusRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class TicketStatusMapper {

    @Autowired
    protected TicketStatusRepositorySD ticketStatusRepositorySD;

    @Mapping(target = "tickets", ignore = true)
    public abstract TicketStatus map(TicketStatusDTO dto);

    public abstract TicketStatusDTO map(TicketStatus entity);

    public TicketStatus map(Short id){
        return ticketStatusRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, TicketStatus.class.getSimpleName()));
    }
}
