package com.ushkov.controller;


import com.ushkov.domain.PassengerPassport;
import com.ushkov.repository.imlp.PassengerPassportRepository;
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

@Api(tags = "PassengerPassport", value="The PassengerPassport API")
@RestController
@RequestMapping("/passengerpassport")
@RequiredArgsConstructor
public class PassengerPassportController {

    private final PassengerPassportRepository repository;

    @ApiOperation(  value = "Find all PassengerPassport`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = PassengerPassport.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<PassengerPassport> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find PassengerPassport`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of PassengerPassport`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = PassengerPassport.class)
    })
    @GetMapping("/id")
    public PassengerPassport findOne(@RequestParam("id") Long id) {
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
                    response = PassengerPassport.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<PassengerPassport> findLimitOffset(@RequestParam("limit") Long limit,
                                                   @RequestParam("offset") Long offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of PassengerPassport`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = PassengerPassport.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<PassengerPassport> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of PassengerPassport`s entities for update",
                    required = true)
            @RequestBody List<PassengerPassport> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one PassengerPassport`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = PassengerPassport.class)
    })
    @PostMapping("/post")
    public PassengerPassport saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PassengerPassport entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update PassengerPassport`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PassengerPassport.class)
    })
    @PutMapping("/put")
    public PassengerPassport updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PassengerPassport entity) {
        return repository.updateOne(entity);
    }
}
