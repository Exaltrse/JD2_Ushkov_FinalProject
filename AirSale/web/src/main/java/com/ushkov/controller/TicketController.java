package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.domain.Ticket;
import com.ushkov.domain.TicketStatus;
import com.ushkov.domain.Users;
import com.ushkov.dto.CurrentFlightDTO;
import com.ushkov.dto.PassportDTO;
import com.ushkov.dto.TicketDTO;
import com.ushkov.dto.TicketStatusDTO;
import com.ushkov.exception.NoPermissionForThisOperationException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.CurrentFlightMapper;
import com.ushkov.mapper.PassportMapper;
import com.ushkov.mapper.TicketMapper;
import com.ushkov.mapper.TicketStatusMapper;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.repository.springdata.TicketRepositorySD;
import com.ushkov.repository.springdata.TicketStatusRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.security.util.TokenUtils;
import com.ushkov.utils.SystemRoles;
import com.ushkov.validation.ValidationGroup;


@Api(tags = "Ticket", value="The Ticket API", description = "The Ticket API")
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class TicketController {

    private final TicketRepositorySD repository;
    private final CurrentFlightRepositorySD currentFlightRepositorySD;
    private final TicketStatusRepositorySD ticketStatusRepositorySD;
    private final UsersRepositorySD usersRepositorySD;
    private final TicketMapper mapper;
    private final PassportMapper passportMapper;
    private final CurrentFlightMapper currentFlightMapper;
    private final TicketStatusMapper ticketStatusMapper;
    private final TokenUtils tokenUtils;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Tickets entries from DB.",
            notes = "Find all not disabled Tickets entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=TicketDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<TicketDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value="Find Ticket entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = TicketDTO.class)
    })
    @GetMapping("/{id}")
    public TicketDTO findOne(
            @Valid
            @Min(1)
            @Max(Long.MAX_VALUE)
            @ApiParam(
                    value = "Id of Ticket entry.",
                    required = true
            )
            @PathVariable
                    long id,
            @RequestHeader("X-Auth-Token") String token) {
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && repository.findAllByUserId(user.getId()).stream().noneMatch(e->e.getId()==id))
            throw new NoPermissionForThisOperationException();
        return mapper.map(repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id, Ticket.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by passport entity.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/findbypassport")
    public Page<TicketDTO> findByPassport(
            @Valid
            @ApiParam(
                    name = "passport",
                    value = "Passport entity.",
                    required = true)
            @RequestBody
                    PassportDTO dto,
            Pageable page) {
        return repository.findAllByPassport(passportMapper.map(dto), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by CurrentFlight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/findbycurrentflight")
    public Page<TicketDTO> findByCurrentFlight(
            @Valid
            @ApiParam(
                    name = "currentflight",
                    value = "CurrentFlight entity.",
                    required = true)
            @RequestBody
                    CurrentFlightDTO dto,
            Pageable page) {
        return repository.findAllByCurrentFlight(currentFlightMapper.map(dto), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by TicketStatus.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/findbyticketstatus")
    public Page<TicketDTO> findByTicketStatus(
            @Valid
            @ApiParam(
                    name = "ticketstatus",
                    value = "TicketStatus entity.",
                    required = true)
            @RequestBody
                    TicketStatusDTO dto,
            Pageable page) {
        return repository.findAllByTicketStatus(ticketStatusMapper.map(dto), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by TicketStatus and CurrentFlight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/findbycurrentflightandticketstatus")
    public Page<TicketDTO> findByCurrentFlightAndTicketStatus(
            @Valid
            @ApiParam(
                    name = "currentflight",
                    value = "CurrentFlight entity.",
                    required = true)
            @RequestBody
                    CurrentFlightDTO currentFlightDTO,
            @Valid
            @ApiParam(
                    name = "ticketstatus",
                    value = "TicketStatus entity.",
                    required = true)
            @RequestBody
                    TicketStatusDTO ticketStatusDTO,
            Pageable page) {

        return repository
                .findAllByCurrentFlightAndTicketStatusAndDisabledIsFalse(
                        currentFlightMapper.map(currentFlightDTO),
                        ticketStatusMapper.map(ticketStatusDTO),
                        page
                ).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<TicketDTO> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
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
    public List<TicketDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of Ticket`s entities for update",
                    required = true)
            @RequestBody List<TicketDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value = "Save one Ticket`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public TicketDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody TicketDTO entity,
            @RequestHeader("X-Auth-Token") String token) {
        entity.setId(null);
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)) {
            entity.setTicketStatus((short) 1);
        }
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Update Ticket`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = TicketDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public TicketDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody TicketDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Update All Ticket`s entity status in DB by current flight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = TicketDTO.class)
    })
    @PatchMapping("/updateticketstatusbycurrentflight")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    @Validated(ValidationGroup.ExistingObject.class)
    public List<TicketDTO> updateStatusByCurrentFlight(
            @Valid
            @Min(1)
            @Max(Long.MAX_VALUE)
            @ApiParam(
                    name = "currentflightid",
                    value = "ID of CurrentFlight for which necessary update status in al not disabled depended tickets.",
                    required = true)
            @RequestBody Long currentFlightId,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "ticketstatusid",
                    value = "ID of TicketStaus which is necessary put in all not disabled Ticket`s entities, that depended of CurrentFlight.",
                    required = true)
            @RequestBody Short ticketStatusId) {
        TicketStatus ticketStatus = ticketStatusRepositorySD.findById(ticketStatusId).orElseThrow(()->new NoSuchEntityException(ticketStatusId, TicketStatus.class.getSimpleName()));
        List<Ticket> ticketList = repository.findAllByCurrentFlightAndDisabledIsFalse(
                currentFlightRepositorySD
                        .findById(currentFlightId)
                        .orElseThrow(()-> new NoSuchEntityException(currentFlightId, CurrentFlight.class.getSimpleName())));
        if(ticketList.isEmpty()) throw new NoSuchEntityException("There are no not disabled ticket for CurrentFlight with ID " + currentFlightId);
        ticketList.forEach(cfre -> cfre.setTicketStatus(ticketStatus));
        return repository.saveAll(ticketList).stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @Valid
            @Positive
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @PathVariable long id){
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @PathVariable List<Long> idList){
        repository.disableEntities(idList);
    }
}
