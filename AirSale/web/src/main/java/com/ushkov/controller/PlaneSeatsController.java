package com.ushkov.controller;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.domain.Plane;
import com.ushkov.domain.PlaneSeats;
import com.ushkov.domain.SeatClass;
import com.ushkov.domain.Ticket;
import com.ushkov.dto.PlaneSeatsDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;
import com.ushkov.repository.springdata.PlaneSeatsRepositorySD;
import com.ushkov.repository.springdata.SeatClassRepositorySD;
import com.ushkov.repository.springdata.TicketRepositorySD;

@Api(tags = "PlaneSeats", value="The PlaneSeats API", description = "The PlaneSeats API")
@RestController
@RequestMapping("/planeseats")
@RequiredArgsConstructor
public class PlaneSeatsController {

    private final PlaneSeatsRepositorySD repository;
    private final PlaneRepositorySD planeRepositorySD;
    private final SeatClassRepositorySD seatClassRepositorySD;
    private final CurrentFlightRepositorySD currentFlightRepositorySD;
    private final TicketRepositorySD ticketRepositorySD;

    @ApiOperation(  value = "Find all not disabled PlaneSeatss entries from DB.",
            notes = "Find all not disabled PlaneSeatss entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=PlaneSeats.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<PlaneSeats> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find PlaneSeats entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of PlaneSeats entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = PlaneSeats.class)
    })
    @GetMapping("/id")
    public PlaneSeats findOne(@RequestParam("id") int id) {

        return repository.findById(id)
                .orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<PlaneSeats> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disabled setas by plane.")
    @GetMapping("/findbyplane")
    public List<PlaneSeats> findByPlane(
            @ApiParam(
                    name = "plane",
                    value = "ID of Plane entity.",
                    required = true)
            @RequestParam
                    int entityId) {
        return repository.findAllByPlaneAndDisabledIsFalse(planeRepositorySD.findById(entityId)
                .orElseThrow(()->new NoSuchEntityException(entityId,Plane.class.getTypeName())));
    }

    @ApiOperation(value = "Find not disabled seats by CurrentFlight and actual count of free seats.")
    @GetMapping("/findbycurrentflight")
    public List<PlaneSeatsDTO> findByCurrentFlight(
            @ApiParam(
                    name = "currentflight",
                    value = "ID of CurrentFlight entity.",
                    required = true)
            @RequestParam
                    Long entityId) {
        CurrentFlight currentFlight = currentFlightRepositorySD.findById(entityId).orElseThrow(()->new NoSuchEntityException(entityId, CurrentFlight.class.getSimpleName()));
        List<Ticket> ticketList = ticketRepositorySD.findAllByCurrentFlight(currentFlight);
        List<PlaneSeats> planeSeatsList = repository.findAllByPlaneAndDisabledIsFalse(currentFlight.getFlightPlane().getPlane());
        if(planeSeatsList.isEmpty())
            throw new NoSuchEntityException("There are no not disabled PlaneSeats entities for that Current Flight Plane");
        Map<SeatClass, Short> planeSeatsMap = new HashMap<>();
        for(PlaneSeats pl : planeSeatsList){
            planeSeatsMap.put(pl.getSeat(), pl.getNumberOfSeats());
        }
        if(ticketList.isEmpty())
            return planeSeatsMap
                    .entrySet()
                    .stream()
                    .map(
                            p->PlaneSeatsDTO.builder()
                                    .seatClass(p.getKey())
                                    .plane(currentFlight.getFlightPlane().getPlane().getId())
                                    .numberOfSeats((short) 0)
                                    .build())
                    .collect(Collectors.toList());
        ticketList.forEach(tl -> planeSeatsMap.put(tl.getSeat().getSeat(), (short) (planeSeatsMap.get(tl.getSeat().getSeat()) - 1)));
        return planeSeatsMap.entrySet().stream().map(psm ->
            new PlaneSeatsDTO()
                    .builder()
                    .seatClass(psm.getKey())
                    .plane(currentFlight.getFlightPlane().getPlane().getId())
                    .numberOfSeats(psm.getValue())
                    .build()
        ).collect(Collectors.toList());

    }



    @ApiOperation(value = "Find not disabled setas by plane and seat class.")
    @GetMapping("/findbyplaneandseatclass")
    public List<PlaneSeats> findByPlaneAndSeatClass(
            @ApiParam(
                    name = "plane",
                    value = "ID of Plane entity.",
                    required = true)
            @RequestParam
                    int planeEntityId,
            @ApiParam(
                    name = "seatclass",
                    value = "ID of SeatClass entity.",
                    required = true)
            @RequestParam
                    short seatClassEntityId) {

        return repository.findByPlaneAndSeatAndDisabledIsFalse(
                planeRepositorySD.findById(planeEntityId).orElseThrow(()->new NoSuchEntityException(planeEntityId, Plane.class.getTypeName())),
                seatClassRepositorySD.findById(seatClassEntityId).orElseThrow(()->new NoSuchEntityException(seatClassEntityId, SeatClass.class.getTypeName())));
    }

    @ApiOperation(  value = "Save list of PlaneSeats`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<PlaneSeats> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of PlaneSeats`s entities for update",
                    required = true)
            @RequestBody List<PlaneSeats> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one PlaneSeats`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public PlaneSeats saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PlaneSeats entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update PlaneSeats`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PlaneSeats.class)
    })
    @PutMapping("/put")
    public PlaneSeats updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PlaneSeats entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody int id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Integer> idList){
        repository.disableEntities(idList);
    }
}
