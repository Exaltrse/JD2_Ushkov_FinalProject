package com.ushkov.controller;


import com.ushkov.domain.FlightPlane;
import com.ushkov.repository.imlp.FlightPlaneRepository;
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

@Api(tags = "FlightPlane", value="The FlightPlane API")
@RestController
@RequestMapping("/flightplane")
@RequiredArgsConstructor
public class FlightPlaneController {

    private final FlightPlaneRepository repository;

    @ApiOperation(  value = "Find all FlightPlane`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = FlightPlane.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<FlightPlane> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find FlightPlane`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of FlightPlane`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = FlightPlane.class)
    })
    @GetMapping("/id")
    public FlightPlane findOne(@RequestParam("id") Long id) {
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
                    response = FlightPlane.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<FlightPlane> findLimitOffset(@RequestParam("limit") Long limit,
                                             @RequestParam("offset") Long offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of FlightPlane`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = FlightPlane.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<FlightPlane> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of FlightPlane`s entities for update",
                    required = true)
            @RequestBody List<FlightPlane> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one FlightPlane`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = FlightPlane.class)
    })
    @PostMapping("/post")
    public FlightPlane saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody FlightPlane entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update FlightPlane`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = FlightPlane.class)
    })
    @PutMapping("/put")
    public FlightPlane updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody FlightPlane entity) {
        return repository.updateOne(entity);
    }
}
