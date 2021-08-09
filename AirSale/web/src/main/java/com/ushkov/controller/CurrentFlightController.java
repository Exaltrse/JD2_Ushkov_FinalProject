package com.ushkov.controller;


import com.ushkov.domain.CurrentFlight;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.CurrentFlightRepositorySD;
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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Api(tags = "CurrentFlight", value="The CurrentFlight API", description = "The CurrentFlight API")
@RestController
@RequestMapping("/currentflight")
@RequiredArgsConstructor
public class CurrentFlightController {

    private final CurrentFlightRepositorySD repository;

    @ApiOperation(  value = "Find all not disabled CurrentFlights entries from DB.",
            notes = "Find all not disabled CurrentFlights entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=CurrentFlight.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<CurrentFlight> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find CurrentFlight entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of CurrentFlight entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = CurrentFlight.class)
    })
    @GetMapping("/id")
    public CurrentFlight findOne(@RequestParam("id") Long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + id.toString()));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<CurrentFlight> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disables entities by departure date.")
    @GetMapping("/findbydeparture")
    public Page<CurrentFlight> findByDeparture(
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
            @ApiParam(
                    name = "status",
                    value = "ID of status of Current Flight.")
            @RequestParam
                    Short currentFlightStatus,
            Pageable page) {
        return repository.findAllByDepartureDateBetweenAndDisabledIsFalse(departureBeginingDate, departureEndDate, currentFlightStatus, page);
    }

    @ApiOperation(value = "Find not disables entities by arrival date.")
    @GetMapping("/findbyarrival")
    public Page<CurrentFlight> findByArrival(
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
            @ApiParam(
                    name = "status",
                    value = "ID of status of Current Flight.")
            @RequestParam
                    Short currentFlightStatus,
            Pageable page) {
        return repository.findAllByArrivalDateBetweenAndDisabledIsFalse(arrivalBeginingDate, arrivalEndDate, currentFlightStatus, page);
    }

    @ApiOperation(value = "Find not disables entities by departure date and Airports of departure and arrival.")
    @GetMapping("/findbyarrivalandairports")
    public Page<CurrentFlight> findByDepartureAndAirports(
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
            @ApiParam(
                    name = "departureairportid",
                    value = "ID of departure airport",
                    required = true)
            @RequestParam
                    Short departureAirportId,
            @ApiParam(
                    name = "arrivalairportid",
                    value = "ID of arrival airport.",
                    required = true)
            @RequestParam
                    Short arrivalAirportId,
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
                page);
    }

    @ApiOperation(  value = "Save list of CurrentFlight`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<CurrentFlight> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of CurrentFlight`s entities for update",
                    required = true)
            @RequestBody List<CurrentFlight> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one CurrentFlight`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public CurrentFlight saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody CurrentFlight entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update CurrentFlight`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = CurrentFlight.class)
    })
    @PutMapping("/put")
    public CurrentFlight updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody CurrentFlight entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(Long id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Long> idList){
        repository.disableEntities(idList);
    }
}
