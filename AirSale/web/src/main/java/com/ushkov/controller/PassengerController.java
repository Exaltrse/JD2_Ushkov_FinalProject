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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.ushkov.domain.Passenger;
import com.ushkov.domain.Users;
import com.ushkov.dto.PassengerDTO;
import com.ushkov.exception.NoPermissionForThisOperationException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.PassengerMapper;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
import com.ushkov.requests.NameLastNameRequest;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.security.util.TokenUtils;
import com.ushkov.utils.SystemRoles;
import com.ushkov.validation.ValidationGroup;




@Api(tags = "Passenger", value="The Passenger API", description = "The Passenger API")
@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class PassengerController {
    private final PassengerRepositorySD repository;
    private final UsersRepositorySD usersRepositorySD;
    private final PassengerMapper mapper;
    private final TokenUtils tokenUtils;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Passengers entries from DB.",
            notes = "Find all not disabled Passengers entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = PassengerDTO.class,
                    responseContainer = "List")
    })
    @GetMapping
    public List<PassengerDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value="Find Passenger entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = PassengerDTO.class)
    })
    @GetMapping("/{id}")
    public PassengerDTO findOne(
            @Valid
            @Min(1)
            @Max(Long.MAX_VALUE)
            @ApiParam(
                    value = "Id of Passenger entry.",
                    required = true
            )
            @PathVariable
                    long id,
            @RequestHeader("X-Auth-Token") String token) {
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && user.getPassengers().contains(repository.findById(id).orElseThrow(NoSuchEntityException::new)))
            throw new NoPermissionForThisOperationException();
        return mapper.map(repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id, "Passenger")));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
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
    @PostMapping("/findbyname")
    public Page<PassengerDTO> findByName(
            @Valid
            @NotEmpty
            @ApiParam(
                    value = "String for searching by name.",
                    required = true)
            @RequestBody
                    String name,
            @ApiIgnore final Pageable page) {
        return repository.findAllByFirstNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by lastname or part of lastname.")
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
    @PostMapping("/findbylastname")
    public Page<PassengerDTO> findByLastname(
            @Valid
            @NotEmpty
            @ApiParam(
                    value = "String for searching by name.",
                    required = true)
            @RequestBody
                    String name,
            Pageable page) {
        return repository.findAllByLastNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by firstname and lastname or part of it.")
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
    @PostMapping("/findbyfirstandlastname")
    public Page<PassengerDTO> findByFirstAndLastname(
            @ApiParam(
                    value = "Full name for searching.",
                    required = true)
            @RequestBody
                    NameLastNameRequest request,
            @ApiIgnore final Pageable page) {
        return repository
                .findByFirstNameIsContainingAndLastNameIsContainingAndDisabledIsFalse(request.getFirstName(), request.getLastName(), page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find passengers by user.")
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
    @PostMapping("/findbyuser")
    public Page<PassengerDTO> findAllPassengersByUser(
            @Valid
            @ApiParam(
                    name = "user",
                    value = "User entity to search for dependent passenger entities.",
                    required = true)
            @RequestBody
                    Integer id,
            @ApiIgnore final Pageable page,
            @RequestHeader("X-Auth-Token") String token){
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && user.getId()!=id)
            throw new NoPermissionForThisOperationException();
        Users searchedUser = usersRepositorySD.findById(id).orElseThrow(NoSuchEntityException::new);
        List<Long> passengersId  = searchedUser.getPassengers().stream().map(Passenger::getId).collect(Collectors.toList());
        return repository.findAllByIdInAndDisabledIsFalse(passengersId, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
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
    public Page<PassengerDTO> findAll(@ApiIgnore final Pageable page) {
        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Save list of Passenger`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<PassengerDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of Passenger`s entities for update",
                    required = true)
            @RequestBody List<PassengerDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Save one Passenger`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public PassengerDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PassengerDTO dto) {
        dto.setId(null);
        return mapper.map(repository.save(mapper.map(dto)));
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value = "Add one Passenger`s entity to User",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/addpassengertouser")
    public boolean addPassengerToUser(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PassengerDTO dto,
            @Valid
            @Min(1)
            @Max(Integer.MAX_VALUE)
            @ApiParam(
                    name = "id",
                    value = "ID of Users entity to which Passenger is adding."
            )
            @RequestBody Integer id,
            @RequestHeader("X-Auth-Token") String token) {
        if(dto.getId()==0) dto.setId(null);
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && user.getId()!=id)
            throw new NoPermissionForThisOperationException();
        Passenger newPassenger = repository.save(mapper.map(dto));
        Users existingUser = usersRepositorySD.findById(id).orElseThrow(NoSuchEntityException::new);
        existingUser.getPassengers().add(newPassenger);
        return true;
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value = "Update Passenger`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PassengerDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public PassengerDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PassengerDTO dto,
            @RequestHeader("X-Auth-Token") String token) {
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && !user.getPassengers().contains(repository.findById(dto.getId()).orElseThrow(NoSuchEntityException::new)))
            throw new NoPermissionForThisOperationException();
        return mapper.map(repository.saveAndFlush(mapper.map(dto)));
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @Valid
            @Positive
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @PathVariable long id,
            @RequestHeader("X-Auth-Token") String token){
        Users user = usersRepositorySD.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)
                && !user.getPassengers().contains(repository.findById(id).orElseThrow(NoSuchEntityException::new)))
            throw new NoPermissionForThisOperationException();
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
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
            @PathVariable List<Long> idList){
        repository.disableEntities(idList);
    }
}
