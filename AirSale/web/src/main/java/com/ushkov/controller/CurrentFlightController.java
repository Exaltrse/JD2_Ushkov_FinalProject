package com.ushkov.controller;


import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.dto.CurrentFlightDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.CurrentFlightMapper;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
import com.ushkov.security.util.SecuredRoles;
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
    public List<CurrentFlightDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value="Find CurrentFlight entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = CurrentFlightDTO.class)
    })
    @GetMapping("/id")
    public CurrentFlightDTO findOne(
            @Valid
            @Min(1)
            @Max(Long.MAX_VALUE)
            @ApiParam(
                    value = "Id of CurrentFlight entry.",
                    required = true)
            @RequestParam("id")
                    Long id) {

        return mapper.map(
                repository
                        .findById(id)
                        .orElseThrow(()-> new NoSuchEntityException(id, CurrentFlight.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<CurrentFlightDTO> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by departure date.")
    @GetMapping("/findbydeparture")
    public Page<CurrentFlightDTO> findByDeparture(
            @ApiParam(
                    name = "departurebeginingdate",
                    value = "Timestamp of departure. From what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp departureBeginingDate,
            @ApiParam(
                    name = "departureenddate",
                    value = "Timestamp of departure. To what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp departureEndDate,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "status",
                    value = "ID of status of Current Flight.")
            @RequestParam
                    Short currentFlightStatus,
            Pageable page) {
        return repository
                .findAllByDepartureDateBetweenAndDisabledIsFalse(
                        departureBeginingDate,
                        departureEndDate,
                        currentFlightStatus,
                        page
                ).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by arrival date.")
    @GetMapping("/findbyarrival")
    public Page<CurrentFlightDTO> findByArrival(
            @ApiParam(
                    name = "arrivalbeginingdate",
                    value = "Timestamp of arrival. From what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp arrivalBeginingDate,
            @ApiParam(
                    name = "arrivalenddate",
                    value = "Timestamp of arrival. To what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp arrivalEndDate,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "status",
                    value = "ID of status of Current Flight.")
            @RequestParam
                    Short currentFlightStatus,
            Pageable page) {
        return repository
                .findAllByArrivalDateBetweenAndDisabledIsFalse(
                        arrivalBeginingDate,
                        arrivalEndDate,
                        currentFlightStatus,
                        page
                ).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by departure date and Airports of departure and arrival.")
    @GetMapping("/findbyarrivalandairports")
    public Page<CurrentFlightDTO> findByDepartureAndAirports(
            @ApiParam(
                    name = "departurebeginingdate",
                    value = "Timestamp of departure. From what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp departureBeginingDate,
            @ApiParam(
                    name = "departureenddate",
                    value = "Timestamp of departure. To what timestamp is searching.",
                    required = true)
            @RequestParam
                    Timestamp departureEndDate,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "departureairportid",
                    value = "ID of departure airport",
                    required = true)
            @RequestParam
                    Short departureAirportId,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "arrivalairportid",
                    value = "ID of arrival airport.",
                    required = true)
            @RequestParam
                    Short arrivalAirportId,
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "status",
                    value = "ID of status of Current Flight.")
            @RequestParam
                    Short currentFlightStatus,
            Pageable page) {
        return repository.findAllByDepartureAndAirports(
                departureBeginingDate,
                departureEndDate,
                departureAirportId,
                arrivalAirportId,
                currentFlightStatus,
                page
        ).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of CurrentFlight`s entities to DB",
            httpMethod = "POST")
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
            @RequestBody List<CurrentFlightDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
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
                    value = "Entity for save",
                    required = true)
            @RequestBody CurrentFlightDTO entity) {
        entity.setId(null);
        return mapper.map(repository.save(mapper.map(entity)));
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
            @RequestBody CurrentFlightDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
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
            @RequestBody long id){
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
            @RequestBody List<Long> idList){
        repository.disableEntities(idList);
    }
}
