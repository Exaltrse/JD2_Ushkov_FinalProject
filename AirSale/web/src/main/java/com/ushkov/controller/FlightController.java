package com.ushkov.controller;


import com.ushkov.domain.Airport;
import com.ushkov.domain.Flight;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirportRepositorySD;
import com.ushkov.repository.springdata.FlightRepositorySD;
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
import java.util.List;

@Api(tags = "Flight", value="The Flight API", description = "The Flight API")
@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightRepositorySD repository;
    private final AirportRepositorySD airportRepositorySD;

    @ApiOperation(  value = "Find all not disabled Flights entries from DB.",
            notes = "Find all not disabled Flights entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Flight.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Flight> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find Flight entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Flight entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Flight.class)
    })
    @GetMapping("/id")
    public Flight findOne(@RequestParam("id") Integer id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + id.toString()));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Flight> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disables entities by flightnumber or part of it.")
    @GetMapping("/findbyflightnumber")
    public Page<Flight> findByFlightNumber(
            @ApiParam(
                    name = "name",
                    value = "String for searching by flightnumber.",
                    required = true)
            @RequestParam
                    String flightnumber,
            Pageable page) {
        return repository.findAllByNumberIsContainingAndDisabledIsFalse(flightnumber, page);
    }

    @ApiOperation(value = "Find not disables entities by airport of departure and arrival.")
    @GetMapping("/findbydepartureandarrival")
    public Page<Flight> findByDepartureAndArrival(
            @ApiParam(
                    name = "departure",
                    value = "ID of airport of departure for searching.",
                    required = true)
            @RequestParam
                    short departureAirportId,
            @ApiParam(
                    name = "arrival",
                    value = "ID of airport of arrival for searching.",
                    required = true)
            @RequestParam
                    short arrivalAirportId,
            Pageable page) {
        Airport departureAirport = airportRepositorySD.findById(departureAirportId)
                .orElseThrow(()->new NoSuchEntityException(departureAirportId, Airport.class.getSimpleName()));
        Airport arrivalAirport = airportRepositorySD.findById(departureAirportId)
                .orElseThrow(()->new NoSuchEntityException(arrivalAirportId, Airport.class.getSimpleName()));
        return repository.findAllByDepartureAndDestinationAndDisabledIsFalse(departureAirport, arrivalAirport, page);
    }


    @ApiOperation(  value = "Save list of Flight`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Flight> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Flight`s entities for update",
                    required = true)
            @RequestBody List<Flight> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Flight`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Flight saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Flight entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Flight`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Flight.class)
    })
    @PutMapping("/put")
    public Flight updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Flight entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(int id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Integer> idList){
        repository.disableEntities(idList);
    }
}
