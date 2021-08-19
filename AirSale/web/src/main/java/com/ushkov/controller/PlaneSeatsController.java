package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.ushkov.domain.PlaneSeats;
import com.ushkov.dto.PlaneSeatsDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.PlaneSeatsMapper;
import com.ushkov.repository.springdata.PlaneSeatsRepositorySD;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.validation.ValidationGroup;

@Api(tags = "PlaneSeats", value="The PlaneSeats API", description = "The PlaneSeats API")
@RestController
@RequestMapping("/planeseats")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class PlaneSeatsController {

    private final PlaneSeatsRepositorySD repository;
    private final PlaneSeatsMapper mapper;

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Find all not disabled PlaneSeatss entries from DB.",
            notes = "Find all not disabled PlaneSeatss entries from DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=PlaneSeatsDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<PlaneSeatsDTO> findAll() {
        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value="Find PlaneSeats entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = PlaneSeatsDTO.class)
    })
    @GetMapping("/{id}")
    public PlaneSeatsDTO findOne(
            @Valid
            @Min(1)
            @Max(Integer.MAX_VALUE)
            @ApiParam(
                    value = "Id of PlaneSeats entry.",
                    required = true
            )
            @PathVariable
                    int id) {

        return mapper.map(repository.findById(id)
                .orElseThrow(()-> new NoSuchEntityException(id, PlaneSeats.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<PlaneSeatsDTO> findAll(@ApiIgnore final Pageable page) {
        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of PlaneSeats`s entities to DB")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<PlaneSeatsDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of PlaneSeats`s entities for saving",
                    required = true)
            @RequestBody List<PlaneSeatsDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one PlaneSeats`s entity to DB")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public PlaneSeatsDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PlaneSeatsDTO entity) {
        entity.setId(null);
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update PlaneSeats`s entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PlaneSeatsDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public PlaneSeatsDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PlaneSeatsDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @Valid
            @Positive
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @PathVariable int id){
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @PathVariable List<Integer> idList){
        repository.disableEntities(idList);
    }
}
