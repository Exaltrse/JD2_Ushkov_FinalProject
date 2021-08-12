package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;

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
import com.ushkov.domain.Passport;
import com.ushkov.domain.Ticket;
import com.ushkov.domain.TicketStatus;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.repository.springdata.TicketRepositorySD;
import com.ushkov.repository.springdata.TicketStatusRepositorySD;


@Api(tags = "Ticket", value="The Ticket API", description = "The Ticket API")
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepositorySD repository;
    private final CurrentFlightRepositorySD currentFlightRepositorySD;
    private final TicketStatusRepositorySD ticketStatusRepositorySD;

    @ApiOperation(  value = "Find all not disabled Tickets entries from DB.",
            notes = "Find all not disabled Tickets entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Ticket.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Ticket> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find Ticket entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Ticket entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Ticket.class)
    })
    @GetMapping("/id")
    public Ticket findOne(@RequestParam("id") long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(value = "Find all entities by passport entity.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @GetMapping("/findbypassport")
    public Page<Ticket> findByPassport(
            @ApiParam(
                    name = "passport",
                    value = "Passport entity.",
                    required = true)
            @RequestParam
                    Passport entity,
            Pageable page) {
        return repository.findAllByPassport(entity, page);
    }

    @ApiOperation(value = "Find all entities by CurrentFlight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbycurrentflight")
    public Page<Ticket> findByCurrentFlight(
            @ApiParam(
                    name = "currentflight",
                    value = "CurrentFlight entity.",
                    required = true)
            @RequestParam
                    CurrentFlight entity,
            Pageable page) {
        return repository.findAllByCurrentFlight(entity, page);
    }

    @ApiOperation(value = "Find all entities by TicketStatus.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyticketstatus")
    public Page<Ticket> findByTicketStatus(
            @ApiParam(
                    name = "ticketstatus",
                    value = "TicketStatus entity.",
                    required = true)
            @RequestParam
                    TicketStatus entity,
            Pageable page) {
        return repository.findAllByTicketStatus(entity, page);
    }

    @ApiOperation(value = "Find all entities by TicketStatus and CurrentFlight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbycurrentflightandticketstatus")
    public Page<Ticket> findByCurrentFlightAndTicketStatus(
            @ApiParam(
                    name = "currentflight",
                    value = "CurrentFlight entity.",
                    required = true)
            @RequestParam
                    CurrentFlight currentFlightEntity,
            @ApiParam(
                    name = "ticketstatus",
                    value = "TicketStatus entity.",
                    required = true)
            @RequestParam
                    TicketStatus ticketStatusEntity,
            Pageable page) {

        return repository.findAllByCurrentFlightAndTicketStatusAndDisabledIsFalse(currentFlightEntity, ticketStatusEntity, page);
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Ticket> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(  value = "Save list of Ticket`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Ticket> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Ticket`s entities for update",
                    required = true)
            @RequestBody List<Ticket> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Ticket`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Ticket saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Ticket entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Ticket`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Ticket.class)
    })
    @PutMapping("/put")
    public Ticket updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Ticket entity) {
        return repository.saveAndFlush(entity);
    }

    @ApiOperation(  value = "Update All Ticket`s entity status in DB by current flight.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Ticket.class)
    })
    @PutMapping("/updateticketstatusbycurrentflight")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Ticket> updateStatusByCurrentFlight(
            @ApiParam(
                    name = "currentflightid",
                    value = "ID of CurrentFlight for which necessary update status in al not disabled depended tickets.",
                    required = true)
            @RequestBody Long currentFlightId,
            @ApiParam(
                    name = "ticketstatusid",
                    value = "ID of TicketStaus which is necessary put in all not disabled Ticket`s entities, that depended of CurrentFlight.",
                    required = true)
            @RequestBody Short ticketStatusId,
            Pageable page) {
        TicketStatus ticketStatus = ticketStatusRepositorySD.findById(ticketStatusId).orElseThrow(()->new NoSuchEntityException(ticketStatusId, TicketStatus.class.getSimpleName()));
        List<Ticket> ticketList = repository.findAllByCurrentFlightAndDisabledIsFalse(
                currentFlightRepositorySD
                        .findById(currentFlightId)
                        .orElseThrow(()-> new NoSuchEntityException(currentFlightId, CurrentFlight.class.getSimpleName())));
        if(ticketList.isEmpty()) throw new NoSuchEntityException("There are no not disabled ticket for CurrentFlight with ID " + currentFlightId);
        ticketList.forEach(cfre -> cfre.setTicketStatus(ticketStatus));
        return repository.saveAll(ticketList);
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
            @RequestBody long id){
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
            @RequestBody List<Long> idList){
        repository.disableEntities(idList);
    }
}
