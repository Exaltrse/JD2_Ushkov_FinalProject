package com.ushkov.controller;


import com.ushkov.domain.Passenger;
import com.ushkov.domain.UserPassenger;
import com.ushkov.domain.Users;
import com.ushkov.exception.ExistingEntityException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.UserPassengerRepositorySD;
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

@Api(tags = "UserPassenger", value="The UserPassenger API", description = "The UserPassenger API")
@RestController
@RequestMapping("/userpassenger")
@RequiredArgsConstructor
public class UserPassengerController {

    private final UserPassengerRepositorySD repository;
    private final UsersRepositorySD usersRepositorySD;
    private final PassengerRepositorySD passengerRepositorySD;

    @ApiOperation(  value = "Find all not disabled UserPassengers entries from DB.",
            notes = "Find all not disabled UserPassengers entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=UserPassenger.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<UserPassenger> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find UserPassenger entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of UserPassenger entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = UserPassenger.class)
    })
    @GetMapping("/id")
    public UserPassenger findOne(@RequestParam("id") long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<UserPassenger> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(  value = "Save list of UserPassenger`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<UserPassenger> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of UserPassenger`s entities for update",
                    required = true)
            @RequestBody List<UserPassenger> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one UserPassenger`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public UserPassenger saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody UserPassenger entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update UserPassenger`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = UserPassenger.class)
    })
    @PutMapping("/put")
    public UserPassenger updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody UserPassenger entity) {
        return repository.saveAndFlush(entity);
    }

    @ApiOperation(
            value = "Add UserPassenger`s entity in DB - adding passenger.",
            notes = "Add UserPassenger`s entity in DB - adding passenger. Passenger entity must be saved in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Users.class)
    })
    @PutMapping("/addpassenger")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void addPassengerToUser(
            @ApiParam(
                    name = "user",
                    value = "User ID for adding passenger entity",
                    required = true)
            @RequestBody
                    int userId,
            @ApiParam(
                    name = "Passenger",
                    value = "Passenger ID for adding to user",
                    required = true)
            @RequestBody
                    long passengerId) {
        if(repository.existsUserPassengerByUserAndPassenger(userId, passengerId)) throw new ExistingEntityException(ExistingEntityException.Cause.ALREADY_EXIST);
        passengerRepositorySD.findById(passengerId).orElseThrow(()->new NoSuchEntityException(passengerId, Passenger.class.getSimpleName()));
        usersRepositorySD.findById(userId).orElseThrow(()->new NoSuchEntityException(userId, Users.class.getSimpleName()));
        UserPassenger userPassenger = new UserPassenger();
        userPassenger.setPassenger(passengerId);
        userPassenger.setUser(userId);
        userPassenger.setDisabled(false);
        userPassenger.setExpired(false);
        repository.save(userPassenger);
    }

    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(long id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Long> idList){
        repository.disableEntities(idList);
    }
}
