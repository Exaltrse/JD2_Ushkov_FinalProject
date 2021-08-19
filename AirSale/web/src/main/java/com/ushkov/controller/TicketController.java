package com.ushkov.controller;


import java.math.BigDecimal;
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
import io.swagger.annotations.ApiImplicitParams;
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
import springfox.documentation.annotations.ApiIgnore;

import com.ushkov.domain.Ticket;
import com.ushkov.domain.TicketStatus;
import com.ushkov.domain.Users;
import com.ushkov.dto.TicketDTO;
import com.ushkov.exception.NoPermissionForThisOperationException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.CurrentFlightMapper;
import com.ushkov.mapper.DiscountMapper;
import com.ushkov.mapper.PassportMapper;
import com.ushkov.mapper.TicketMapper;
import com.ushkov.mapper.TicketStatusMapper;
import com.ushkov.repository.springdata.TicketRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
import com.ushkov.requests.TicketCurrentFlightAndStatusRequest;
import com.ushkov.requests.TicketPriceRequest;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.security.util.TokenUtils;
import com.ushkov.service.TicketService;
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
    private final UsersRepositorySD usersRepositorySD;
    private final TicketMapper mapper;
    private final PassportMapper passportMapper;
    private final CurrentFlightMapper currentFlightMapper;
    private final TicketStatusMapper ticketStatusMapper;
    private final DiscountMapper discountMapper;
    private final TokenUtils tokenUtils;
    private final TicketService ticketService;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Tickets entries from DB.",
            notes = "Find all not disabled Tickets entries from DB.")
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
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = TicketDTO.class)
    })
    @PostMapping("/id")
    public TicketDTO findOne(
            @Valid
            @Min(1)
            @Max(Long.MAX_VALUE)
            @ApiParam(
                    value = "Id of Ticket entry.",
                    required = true
            )
            @RequestBody
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/findbypassport")
    public Page<TicketDTO> findByPassport(
            @Valid
            @ApiParam(
                    name = "passport",
                    value = "Passport entity.",
                    required = true)
            @RequestBody
                    long dto,
            @ApiIgnore final Pageable page) {
        return repository.findAllByPassport(passportMapper.mapFromId(dto), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by CurrentFlight.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/findbycurrentflight")
    public Page<TicketDTO> findByCurrentFlight(
            @Valid
            @ApiParam(
                    name = "currentflight",
                    value = "CurrentFlight entity.",
                    required = true)
            @RequestBody
                    long dto,
            @ApiIgnore final Pageable page) {
        return repository.findAllByCurrentFlight(currentFlightMapper.mapFromId(dto), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by TicketStatus.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/findbyticketstatus")
    public Page<TicketDTO> findByTicketStatus(
            @Valid
            @ApiParam(
                    value = "TicketStatus entity.",
                    required = true)
            @RequestBody
                    short ticketStatus,
            @ApiIgnore final Pageable page) {
        return repository.findAllByTicketStatus(ticketStatusMapper.mapFromId(ticketStatus), page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find all entities by TicketStatus and CurrentFlight.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/findbycurrentflightandticketstatus")
    public Page<TicketDTO> findByCurrentFlightAndTicketStatus(
             @ApiParam(
                    value = "ID of TicketStatus entity.",
                    required = true)
            @RequestBody
                     TicketCurrentFlightAndStatusRequest request,
            @ApiIgnore final Pageable page) {

        return repository
                .findAllByCurrentFlightAndTicketStatusAndDisabledIsFalse(
                        currentFlightMapper.mapFromId(request.getCurrentFlightDTO()),
                        ticketStatusMapper.mapFromId(request.getTicketStatusDTO()),
                        page
                ).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<TicketDTO> findAll(@ApiIgnore final Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Get Price for ticket")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @PostMapping("/price")
    public BigDecimal getPrice(
            @Valid
            @ApiParam(
                    value = "Info for calculate Ticket Price.",
                    required = true)
            @RequestBody
                    TicketPriceRequest ticketpricerequest) {
        return ticketService.getPrice(ticketpricerequest.getCurrentFlight(), ticketpricerequest.getDiscount());
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
        TicketStatus ticketStatus = ticketStatusMapper.mapFromId(ticketStatusId);
        List<Ticket> ticketList = repository.findAllByCurrentFlightAndDisabledIsFalse(currentFlightMapper.mapFromId(currentFlightId));
        if(ticketList.isEmpty()) throw new NoSuchEntityException("There are no not disabled ticket for CurrentFlight with ID " + currentFlightId);
        ticketList.forEach(cfre -> cfre.setTicketStatus(ticketStatus));
        return repository.saveAll(ticketList).stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Update All Ticket`s with corresponding statuses in DB by current flight.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = TicketDTO.class)
    })
    @PatchMapping("/updateticketstatusbystatusesandcurrentflight")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    @Validated(ValidationGroup.ExistingObject.class)
    public List<TicketDTO> updateStatusByCurrentFlightAndListOfStatus(
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
            @RequestBody Short ticketStatusId,
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "ticketstatusidforsearch",
                    value = "ID of TicketStaus which is necessary put in all not disabled Ticket`s entities, that depended of CurrentFlight.",
                    required = true)
            @RequestBody List<Short> ticketStatusIdList
            ) {
        TicketStatus ticketStatus = ticketStatusMapper.mapFromId(ticketStatusId);
        List<TicketStatus> ticketStatusList = ticketStatusIdList.stream().map(ticketStatusMapper::mapFromId).collect(Collectors.toList());
        List<Ticket> ticketList = repository.findAllByTicketStatusesAndCurrentFlightAndDisableIsFalse(currentFlightId, ticketStatusList);
        if(ticketList.isEmpty()) throw new NoSuchEntityException("There are no not disabled ticket for status update.");
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
