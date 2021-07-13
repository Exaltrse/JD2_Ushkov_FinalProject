package com.ushkov.controller;


import com.ushkov.domain.CurrentFlight;
import com.ushkov.repository.imlp.CurrentFlightRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "CurrentFlight", value="The CurrentFlight API")
@RestController
@RequestMapping("/currentflight")
@RequiredArgsConstructor
public class CurrentFlightController {

    private final CurrentFlightRepository repository;

    @ApiOperation(  value = "Find all CurrentFlight`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = CurrentFlight.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<CurrentFlight> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find CurrentFlight`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of CurrentFlight`s entry.",
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
        return repository.findOne(id);
    }

    @ApiOperation(  value = "Find [limit] entries from DB with [offset].",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "limit",
                    dataType = "string",
                    paramType = "query",
                    value = "Limit entries in result list",
                    required = true),
            @ApiImplicitParam(
                    name = "offset",
                    dataType = "string",
                    paramType = "query",
                    value = "Offset from the beginning of results.",
                    required = true),
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.",
                    response = CurrentFlight.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<CurrentFlight> findLimitOffset(@RequestParam("limit") Long limit,
                                         @RequestParam("offset") Long offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of CurrentFlight`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = CurrentFlight.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
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
                    message = "Entity saved successfully.",
                    response = CurrentFlight.class)
    })
    @PostMapping("/post")
    public CurrentFlight saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody CurrentFlight entity) {
        return repository.saveOne(entity);
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
        return repository.updateOne(entity);
    }
}
