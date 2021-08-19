package com.ushkov.controller;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.dto.CurrentFlightDTO;
import com.ushkov.dto.PlaneSeatsSmallDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.CurrentFlightMapper;
import com.ushkov.mapper.TimestampMapper;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.requests.CurrentFLightSaveOneRequest;
import com.ushkov.requests.CurrentFlightFindOneRequest;
import com.ushkov.requests.CurrentFlightSearchingByTimeAndStatusRequest;
import com.ushkov.requests.CurrentFlightSearchingWithAirportsRequest;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.service.CurrentFlightService;
import com.ushkov.utils.TimestampUtils;
import com.ushkov.validation.TimestampException;
import com.ushkov.validation.ValidationGroup;

@Api(tags = "CurrentFlight", value="The CurrentFlight API", description = "The CurrentFlight API")
@RestController
@RequestMapping("/currentflight")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class CurrentFlightController {

    private final CurrentFlightRepositorySD repository;
    private final CurrentFlightMapper mapper;
    private final CurrentFlightService currentFlightService;
    private final TimestampMapper timestampMapper;


    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disabled CurrentFlights entries from DB.",
            notes = "Find all not disabled CurrentFlights entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=CurrentFlightDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<CurrentFlightDTO> findAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    value = "Client`s TimeZone.",
                    required = true
            )
            @PathVariable String timezoneid) {

        return repository.findAllByDisabledIsFalse()
                .stream().map(e -> mapper.mapFrom(e, TimeZone.getTimeZone(timezoneid))).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value="Find CurrentFlight entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = CurrentFlightDTO.class)
    })
    @PostMapping("/{id}")
    public CurrentFlightDTO findOne(
            @ApiParam(
                    value = "Current flight ID with Client`s TimeZone.",
                    required = true
            )
            @RequestBody CurrentFlightFindOneRequest currentFlightFindOneRequest) {

        return mapper.mapFrom(
                repository
                        .findById(currentFlightFindOneRequest.getId())
                        .orElseThrow(()-> new NoSuchEntityException(currentFlightFindOneRequest.getId(), CurrentFlight.class.getSimpleName())),
                TimeZone.getTimeZone(currentFlightFindOneRequest.getTimezoneid()));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @PostMapping("/page")
    public Page<CurrentFlightDTO> findAll(
            @ApiParam(
                    value = "Client`s TimeZone.",
                    required = true
            )
            @RequestBody String timezoneid,
            @ApiIgnore final Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(e->mapper.mapFrom(e, TimeZone.getTimeZone(timezoneid)));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by departure date.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @PostMapping("/findbydeparture")
    public Page<CurrentFlightDTO> findByDeparture(
            @ApiParam(
                    value = "Request for searching by date/time and status.",
                    required = true
            )
            @RequestBody CurrentFlightSearchingByTimeAndStatusRequest request,
            @ApiIgnore final Pageable page) {
        Timestamp beginning = timestampMapper.doMap(request.getBeginingDate());
        Timestamp ending = timestampMapper.doMap(request.getEndDate());
        if(beginning.after(ending)) throw new TimestampException("departurebeginingdate", "departureenddate");
        return repository
                .findAllByDepartureDateBetweenAndDisabledIsFalse(
                        TimestampUtils.toBeginningOfDay(beginning),
                        TimestampUtils.toEndingOfDay(ending),
                        request.getCurrentFlightStatus(),
                        page
                ).map(e -> mapper.mapFrom(e, TimeZone.getTimeZone(request.getTimezoneid())));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by arrival date.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @PostMapping("/findbyarrival")
    public Page<CurrentFlightDTO> findByArrival(
            @ApiParam(
                    value = "Request for searching by date/time and status.",
                    required = true
            )
            @RequestBody CurrentFlightSearchingByTimeAndStatusRequest request,
            @ApiIgnore final Pageable page) {
        Timestamp beginning = timestampMapper.doMap(request.getBeginingDate());
        Timestamp ending = timestampMapper.doMap(request.getEndDate());
        if(beginning.after(ending)) throw new TimestampException("arrivalbeginingdate", "arrivalenddate");
        return repository
                .findAllByArrivalDateBetweenAndDisabledIsFalse(
                        TimestampUtils.toBeginningOfDay(beginning),
                        TimestampUtils.toEndingOfDay(ending),
                        request.getCurrentFlightStatus(),
                        page
                ).map(e -> mapper.mapFrom(e, TimeZone.getTimeZone(request.getTimezoneid())));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by departure date and Airports of departure and arrival.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @PostMapping("/findbyarrivalandairports")
    public Page<CurrentFlightDTO> findByDepartureAndAirports(
            @ApiParam(
                    value = "",
                    required = true)
            @RequestBody
                    CurrentFlightSearchingWithAirportsRequest request,
            @ApiIgnore final Pageable page) {
        Timestamp beginning = timestampMapper.doMap(request.getBeginingDate());
        Timestamp ending = timestampMapper.doMap(request.getEndDate());
        if(beginning.after(ending)) throw new TimestampException("departurebeginingdate", "departureenddate");
        return repository.findAllByDepartureAndAirports(
                TimestampUtils.toBeginningOfDay(beginning),
                TimestampUtils.toEndingOfDay(ending),
                request.getDepartureAirportId(),
                request.getArrivalAirportId(),
                request.getCurrentFlightStatus(),
                page
        ).map(e -> mapper.mapFrom(e, TimeZone.getTimeZone(request.getTimezoneid())));
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value = "Get info about free seats.",
            notes = "Get information about free seats in List of PlaneSeatsSmallDTO, " +
                    "that represent fre seats by seat class.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Calculating ended successfully.")
    })
    @PostMapping("/calculatefreeseats")
    public List<PlaneSeatsSmallDTO> getFreeSeats(
            @Valid
            @Min(1)
            @ApiParam(
                    name = "currentflightid",
                    value = "ID of Current Flight for calculating.")
            @RequestBody
            Long currentFlightId){
        return currentFlightService.getActualSeatsByCurrentFlight(currentFlightId);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of CurrentFlight`s entities to DB")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<CurrentFlightDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of CurrentFlight`s entities for update",
                    required = true)
            @RequestBody List<CurrentFlightDTO> entities,
            @Valid
            @NotEmpty
            @ApiParam(
                    value = "Client`s TimeZone.",
                    required = true
            )
            @RequestBody String timezoneid) {
        entities.forEach(e->{
            e.setId(null);
            e.setCurrentFlightsStatus((short) 1);});
        return repository.saveAll(entities.stream().map(e-> mapper.mapTo(e)).collect(Collectors.toList()))
                .stream().map(e -> mapper.mapFrom(e, TimeZone.getTimeZone(timezoneid))).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one CurrentFlight`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public CurrentFlightDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody CurrentFLightSaveOneRequest entity) {
        entity.getEntity().setId(null);
        entity.getEntity().setCurrentFlightsStatus((short) 1);
        return mapper.mapFrom(repository.save(mapper.mapTo(entity.getEntity())), TimeZone.getTimeZone(entity.getTimezoneid()));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update CurrentFlight`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = CurrentFlightDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public CurrentFlightDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody CurrentFLightSaveOneRequest entity) {
        return mapper.mapFrom(repository.saveAndFlush(mapper.mapTo(entity.getEntity())), TimeZone.getTimeZone(entity.getTimezoneid()));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
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
