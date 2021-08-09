package com.ushkov.controller;


import com.ushkov.domain.Airline;
import com.ushkov.domain.Airport;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirportRepositorySD;
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

@Api(tags = "Airport", value="The Airport API", description = "The Airport API")
@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportRepositorySD repository;

    @ApiOperation(  value = "Find all not disabled Airports entries from DB.",
            notes = "Find all not disabled Airports entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Airport.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Airport> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find Airport entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Airport entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Airport.class)
    })
    @GetMapping("/id")
    public Airport findOne(@RequestParam("id") Short id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + id.toString()));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Airport> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyname")
    public Page<Airline> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByNameIsContainingAndDisabledIsFalse(name, page);
    }

    @ApiOperation(value = "Find not disables entities by shortname or part of shortname.")
    @GetMapping("/findbyshortname")
    public Page<Airline> findByShortname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByShortNameIsContainingAndDisabledIsFalse(name, page);
    }

    @ApiOperation(  value = "Save list of Airport`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Airport> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Airport`s entities for update",
                    required = true)
            @RequestBody List<Airport> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Airport`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Airport saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Airport entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Airport`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Airport.class)
    })
    @PutMapping("/put")
    public Airport updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Airport entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(Short id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Short> idList){
        repository.disableEntities(idList);
    }
}
