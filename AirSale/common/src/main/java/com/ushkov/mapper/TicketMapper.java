package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Ticket;
import com.ushkov.dto.TicketDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.TicketRepositorySD;

@Component
@Mapper(
        componentModel = "spring",
        uses = {
                PassportMapper.class,
                CurrentFlightMapper.class,
                DiscountMapper.class,
                TicketStatusMapper.class,
                PlaneSeatsMapper.class
        })
public abstract class TicketMapper {

    @Autowired
    protected TicketRepositorySD ticketRepositorySD;

    public abstract Ticket map(TicketDTO dto);

    @Mapping(target = "passport", source = "passport.id")
    @Mapping(target = "currentFlight", source = "currentFlight.id")
    @Mapping(target = "discount", source = "discount.id")
    @Mapping(target = "ticketStatus", source = "ticketStatus.id")
    @Mapping(target = "seat", source = "seat.id")
    public abstract TicketDTO map(Ticket entity);

    public Ticket mapFromId(Long id){
        return ticketRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Ticket.class.getSimpleName()));
    }
}
