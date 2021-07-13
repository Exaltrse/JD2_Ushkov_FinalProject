package com.ushkov.controller;


import com.ushkov.domain.CurrentFlightStatus;
import com.ushkov.repository.imlp.CurrentFlightStatusRepository;
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

@Api(tags = "CurrentFlightStatus", value="The CurrentFlightStatus API")
@RestController
@RequestMapping("/currentflightstatus")
@RequiredArgsConstructor
public class CurrentFlightStatusController {
    private final CurrentFlightStatusRepository repository;

    @ApiOperation(  value = "Find all CurrentFlightStatus`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = CurrentFlightStatus.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<CurrentFlightStatus> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find CurrentFlightStatus`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of CurrentFlightStatus`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = CurrentFlightStatus.class)
    })
    @GetMapping("/id")
    public CurrentFlightStatus findOne(@RequestParam("id") Short id) {
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
                    response = CurrentFlightStatus.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<CurrentFlightStatus> findLimitOffset(@RequestParam("limit") Short limit,
                                                     @RequestParam("offset") Short offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of CurrentFlightStatus`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = CurrentFlightStatus.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<CurrentFlightStatus> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of CurrentFlightStatus`s entities for update",
                    required = true)
            @RequestBody List<CurrentFlightStatus> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one CurrentFlightStatus`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = CurrentFlightStatus.class)
    })
    @PostMapping("/post")
    public CurrentFlightStatus saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody CurrentFlightStatus entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update CurrentFlightStatus`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = CurrentFlightStatus.class)
    })
    @PutMapping("/put")
    public CurrentFlightStatus updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody CurrentFlightStatus entity) {
        return repository.updateOne(entity);
    }
}
