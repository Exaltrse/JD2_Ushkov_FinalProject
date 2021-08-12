package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.Airline;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirlineRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;
import com.ushkov.security.util.SecuredRoles;

@Api(tags = "Airline", value="The Airline API", description = "The Airline API")
@RestController
@RequestMapping("/airline")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
public class AirlineController {

    private final AirlineRepositorySD repository;
    private final PlaneRepositorySD planeRepositorySD;

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disabled Airlines entries from DB.",
                    notes = "Find all not disabled Airlines entries from DB.",
                    httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Airline.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Airline> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value="Find Airline entry from DB by ID.",
                    notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
                    httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of airline entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Airline.class)
    })
    @GetMapping("/id")
    public Airline findOne(@RequestParam("id") Short id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + id.toString()));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Airline> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
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

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
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

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by planes.")
    @GetMapping("/findbyplanes")
    public Page<Airline> findByPlanes(
            @ApiParam(
                    name = "planeidlist",
                    value = "List of ID of planes.",
                    required = true)
            @RequestParam
            List<Integer> planeIdList,
            Pageable page) {
        planeIdList.stream().forEach(pil->planeRepositorySD.findById(pil).orElseThrow());
        return repository.findAllByPlaneAndDisabledIsFalse(
                planeIdList,
                page);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of Airline`s entities to DB",
                    httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Airline> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Airline`s entities for update",
                    required = true)
            @RequestBody List<Airline> entities) {
        return repository.saveAll(entities);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one Airline`s entity to DB",
                    httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public Airline saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Airline entity) {
        return repository.save(entity);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update Airline`s entity in DB.",
                    httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Airline.class)
    })
    @PutMapping()
    public Airline updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Airline entity) {
        return repository.saveAndFlush(entity);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
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
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableAll(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Short> idList){
        repository.disableEntities(idList);
    }
}
