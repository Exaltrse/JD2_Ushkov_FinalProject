//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.Users;
//import com.ushkov.repository.imlp.UserRepository;
//import com.ushkov.utils.OldController;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@OldController
//@Api(tags = "User", value="The User API")
//@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserRepository repository;
//
//    @ApiOperation(  value = "Find all User`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = Users.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<Users> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find User`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of User`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = Users.class)
//    })
//    @GetMapping("/id")
//    public Users findOne(@RequestParam("id") Integer id) {
//        return repository.findOne(id);
//    }
//
//    @ApiOperation(  value = "Find [limit] entries from DB with [offset].",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "limit",
//                    dataType = "string",
//                    paramType = "query",
//                    value = "Limit entries in result list",
//                    required = true),
//            @ApiImplicitParam(
//                    name = "offset",
//                    dataType = "string",
//                    paramType = "query",
//                    value = "Offset from the beginning of results.",
//                    required = true),
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entries found successfully.",
//                    response = Users.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<Users> findLimitOffset(@RequestParam("limit") Integer limit,
//                                      @RequestParam("offset") Integer offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of User`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = Users.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<Users> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of User`s entities for update",
//                    required = true)
//            @RequestBody List<Users> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one User`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = Users.class)
//    })
//    @PostMapping("/post")
//    public Users saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody Users entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update User`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = Users.class)
//    })
//    @PutMapping("/put")
//    public Users updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody Users entity) {
//        return repository.updateOne(entity);
//    }
//}
