package com.ushkov.controller;

import com.ushkov.domain.Users;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.RoleRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
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

import java.sql.SQLException;
import java.util.List;

@Api(tags = "Users", value="The Users API", description = "The Users API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersRepositorySD repository;
    private final RoleRepositorySD roleRepository;


    @ApiOperation(  value = "Find all not disabled Userss entries from DB.",
            notes = "Find all not disabled Userss entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Users.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Users> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find Users entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Users entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Users.class)
    })
    @GetMapping("/id")
    public Users findOne(@RequestParam("id") int id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Users> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyloginpart")
    public Page<Users> findByLoginpart(
            @ApiParam(
                    name = "login",
                    value = "String for searching by login.",
                    required = true)
            @RequestParam
                    String login,
            Pageable page) {
        return repository.findAllByLoginIsContainingAndDisabledIsFalse(login, page);
    }

    @ApiOperation(value = "Find user by full login.")
    @GetMapping("/findbynamedistinct")
    public Users findByLoginDistinct(
            @ApiParam(
                    name = "login",
                    value = "String for searching by login.",
                    required = true)
            @RequestParam
            String login){
        return repository.findByLogin(login);
    }

    @ApiOperation(value = "Find not disables entities by Roles.")
    @GetMapping("/findbyroles")
    public Page<Users> findByShortname(
            @ApiParam(
                    name = "role",
                    value = "ID of role.",
                    required = true)
            @RequestParam
                    short roleId,
            Pageable page) {
        return repository.findAllByRole(roleRepository.findById(roleId).orElseThrow(()->new NoSuchEntityException(roleId, "Role")), page);
    }

    @ApiOperation(  value = "Save list of Users`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Users> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Users`s entities for update",
                    required = true)
            @RequestBody List<Users> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Users`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Users saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Users entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Users`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Users.class)
    })
    @PutMapping("/put")
    public Users updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Users entity) {
        return repository.saveAndFlush(entity);
    }

    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(int id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Integer> idList){
        repository.disableEntities(idList);
    }
}
