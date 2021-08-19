package com.ushkov.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.domain.Plane;
import com.ushkov.domain.PlaneSeats;
import com.ushkov.domain.TicketStatus;
import com.ushkov.dto.PlaneSeatsSmallDTO;
import com.ushkov.exception.CustomException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.SeatClassMapper;
import com.ushkov.mapper.TicketStatusMapper;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.repository.springdata.PlaneSeatsRepositorySD;
import com.ushkov.repository.springdata.TicketRepositorySD;

@Service
@RequiredArgsConstructor
public class CurrentFlightService {

    private final CurrentFlightRepositorySD repository;
    private final PlaneSeatsRepositorySD planeSeatsRepositorySD;
    private final TicketRepositorySD ticketRepositorySD;
    private final TicketStatusMapper ticketStatusMapper;
    private final SeatClassMapper seatClassMapper;

    public List<PlaneSeatsSmallDTO> getActualSeatsByCurrentFlight(Long id){
        CurrentFlight currentFlight = repository.findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, CurrentFlight.class.getSimpleName()));
        Plane plane = currentFlight.getFlightPlane().getPlane();
        List<PlaneSeats> planeSeatsList = planeSeatsRepositorySD.findAllByPlaneAndDisabledIsFalse(plane);
        if(planeSeatsList.isEmpty()) throw new NoSuchEntityException(
                "There are no information about this CurrentFlight seats at the plane. ID of Current Flight: "
                        + id
                        + ". ID of Plane: "
                        + plane.getId()
                        + "."
        );
        List<TicketStatus> ticketsStatusList = new ArrayList<>();
        //Some hardcode for status "paid"
        ticketsStatusList.add(ticketStatusMapper.mapFromId((short) 2));
        List<PlaneSeatsSmallDTO> planeSeatsSmallDTOList = new ArrayList<>();
        for(PlaneSeats ps : planeSeatsList){
            Short numberOfSoldSeats =
                    ticketRepositorySD.countSoldSeatsByCurrentFlightTicketStatusesAndPlaneSeat(
                            currentFlight, ticketsStatusList, ps);
            PlaneSeatsSmallDTO planeSeatsSmallDTO = new PlaneSeatsSmallDTO();
            planeSeatsSmallDTO.setPlane(plane.getId());
            planeSeatsSmallDTO.setSeatClass(seatClassMapper.map(ps.getSeat()));
            planeSeatsSmallDTO.setNumberOfSeats((short) (ps.getNumberOfSeats()-numberOfSoldSeats));
            planeSeatsSmallDTOList.add(planeSeatsSmallDTO);
        }
        if(planeSeatsSmallDTOList.isEmpty())
            throw new CustomException("Something wrong due procedure of calculating free seats for Plane ID: "
                    + plane.getId()
                    + ", CurrentFlight ID: "
                    + id
                    + ".");
        return planeSeatsSmallDTOList;
    }
}
