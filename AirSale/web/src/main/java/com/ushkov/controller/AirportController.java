package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.Airport;
import com.ushkov.dto.AirportDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.AirportMapper;
import com.ushkov.repository.springdata.AirportRepositorySD;
import com.ushkov.security.util.SecuredRoles;

@Api(tags = "Airport", value="The Airport API", description = "The Airport API")
@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class AirportController {

    private final AirportRepositorySD repository;
    private final AirportMapper mapper;

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disabled Airports entries from DB.",
            notes = "Find all not disabled Airports entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=AirportDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<AirportDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
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
                    response = AirportDTO.class)
    })
    @GetMapping("/id")
    public AirportDTO findOne(@RequestParam("id") Short id) {

        return mapper.map((repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id, Airport.class.getSimpleName()))));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<AirportDTO> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyname")
    public Page<AirportDTO> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by shortname or part of shortname.")
    @GetMapping("/findbyshortname")
    public Page<AirportDTO> findByShortname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByShortNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of Airport`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<AirportDTO> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Airport`s entities for update",
                    required = true)
            @RequestBody List<AirportDTO> entities) {
        return repository
                .saveAll(
                        entities
                                .stream()
                                .map(mapper::map)
                                .collect(Collectors.toList())
                ).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one Airport`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public AirportDTO saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody AirportDTO entity) {
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update Airport`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = AirportDTO.class)
    })
    @PatchMapping()
    public AirportDTO updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody AirportDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody Short id){
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Short> idList){
        repository.disableEntities(idList);
    }
}
