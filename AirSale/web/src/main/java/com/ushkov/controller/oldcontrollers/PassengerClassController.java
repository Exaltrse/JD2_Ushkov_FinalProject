//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.PassengerClass;
//import com.ushkov.repository.imlp.PassengerClassRepository;
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
//@Api(tags = "PassengerClass", value="The PassengerClass API")
//@RestController
//@RequestMapping("/passengerclass")
//@RequiredArgsConstructor
//public class PassengerClassController {
//
//    private final PassengerClassRepository repository;
//
//    @ApiOperation(  value = "Find all PassengerClass`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = PassengerClass.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<PassengerClass> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find PassengerClass`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of PassengerClass`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = PassengerClass.class)
//    })
//    @GetMapping("/id")
//    public PassengerClass findOne(@RequestParam("id") Short id) {
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
//                    response = PassengerClass.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<PassengerClass> findLimitOffset(@RequestParam("limit") Short limit,
//                                                @RequestParam("offset") Short offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of PassengerClass`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = PassengerClass.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<PassengerClass> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of PassengerClass`s entities for update",
//                    required = true)
//            @RequestBody List<PassengerClass> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one PassengerClass`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = PassengerClass.class)
//    })
//    @PostMapping("/post")
//    public PassengerClass saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody PassengerClass entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update PassengerClass`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = PassengerClass.class)
//    })
//    @PutMapping("/put")
//    public PassengerClass updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody PassengerClass entity) {
//        return repository.updateOne(entity);
//    }
//}
