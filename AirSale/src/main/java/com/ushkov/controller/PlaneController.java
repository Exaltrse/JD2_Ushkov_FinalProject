package com.ushkov.controller;


import com.ushkov.domain.Plane;
import com.ushkov.repository.imlp.PlaneRepository;
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

@Api(tags = "Plane", value="The Plane API")
@RestController
@RequestMapping("/plane")
@RequiredArgsConstructor
public class PlaneController {

    private final PlaneRepository repository;

    @ApiOperation(  value = "Find all Plane`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = Plane.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Plane> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find Plane`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Plane`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Plane.class)
    })
    @GetMapping("/id")
    public Plane findOne(@RequestParam("id") Integer id) {
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
                    response = Plane.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<Plane> findLimitOffset(@RequestParam("limit") Integer limit,
                                       @RequestParam("offset") Integer offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of Plane`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = Plane.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<Plane> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Plane`s entities for update",
                    required = true)
            @RequestBody List<Plane> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Plane`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = Plane.class)
    })
    @PostMapping("/post")
    public Plane saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Plane entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update Plane`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Plane.class)
    })
    @PutMapping("/put")
    public Plane updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Plane entity) {
        return repository.updateOne(entity);
    }
}
