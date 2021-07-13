package com.ushkov.controller;


import com.ushkov.domain.Ticket;
import com.ushkov.repository.imlp.TicketRepository;
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

@Api(tags = "Ticket", value="The Ticket API")
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository repository;

    @ApiOperation(  value = "Find all Ticket`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = Ticket.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Ticket> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find Ticket`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Ticket`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Ticket.class)
    })
    @GetMapping("/id")
    public Ticket findOne(@RequestParam("id") Long id) {
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
                    response = Ticket.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<Ticket> findLimitOffset(@RequestParam("limit") Long limit,
                                        @RequestParam("offset") Long offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of Ticket`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = Ticket.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<Ticket> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Ticket`s entities for update",
                    required = true)
            @RequestBody List<Ticket> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Ticket`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = Ticket.class)
    })
    @PostMapping("/post")
    public Ticket saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Ticket entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update Ticket`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Ticket.class)
    })
    @PutMapping("/put")
    public Ticket updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Ticket entity) {
        return repository.updateOne(entity);
    }
}
