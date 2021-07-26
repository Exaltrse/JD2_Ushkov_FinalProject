package com.ushkov.controller;


import com.ushkov.domain.Discount;
import com.ushkov.repository.imlp.DiscountRepository;
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

@Api(tags = "Discount", value="The Discount API")
@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountRepository repository;

    @ApiOperation(  value = "Find all Discount`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = Discount.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Discount> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find Discount`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Discount`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Discount.class)
    })
    @GetMapping("/id")
    public Discount findOne(@RequestParam("id") Short id) {
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
                    response = Discount.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<Discount> findLimitOffset(@RequestParam("limit") Short limit,
                                          @RequestParam("offset") Short offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of Discount`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = Discount.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<Discount> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Discount`s entities for update",
                    required = true)
            @RequestBody List<Discount> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Discount`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = Discount.class)
    })
    @PostMapping("/post")
    public Discount saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Discount entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update Discount`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Discount.class)
    })
    @PutMapping("/put")
    public Discount updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Discount entity) {
        return repository.updateOne(entity);
    }
}