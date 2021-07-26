package com.ushkov.controller;


import com.ushkov.domain.Role;
import com.ushkov.repository.imlp.RoleRepository;
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

@Api(tags = "Role", value="The Role API")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository repository;

    @ApiOperation(  value = "Find all Role`s entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = Role.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Role> findAll() {
        return repository.findAll();
    }

    @ApiOperation(  value="Find Role`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Role`s entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Role.class)
    })
    @GetMapping("/id")
    public Role findOne(@RequestParam("id") Short id) {
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
                    response = Role.class,
                    responseContainer = "List")
    })
    @GetMapping("/limitoffset")
    public List<Role> findLimitOffset(@RequestParam("limit") Short limit,
                                      @RequestParam("offset") Short offset) {
        return repository.findLimitOffset(limit, offset);
    }

    @ApiOperation(  value = "Save list of Role`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.",
                    response = Role.class,
                    responseContainer = "List")
    })
    @PostMapping("/postall")
    public List<Role> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Role`s entities for update",
                    required = true)
            @RequestBody List<Role> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Role`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.",
                    response = Role.class)
    })
    @PostMapping("/post")
    public Role saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Role entity) {
        return repository.saveOne(entity);
    }

    @ApiOperation(  value = "Update Role`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Role.class)
    })
    @PutMapping("/put")
    public Role updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Role entity) {
        return repository.updateOne(entity);
    }
}