//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.Passport;
//import com.ushkov.repository.imlp.PassportRepository;
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
//@Api(tags = "Passport", value="The Passport API")
//@RestController
//@RequestMapping("/Passport")
//@RequiredArgsConstructor
//public class PassportController {
//
//    private final PassportRepository repository;
//
//    @ApiOperation(  value = "Find all Passport`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = Passport.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<Passport> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find Passport`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of Passport`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = Passport.class)
//    })
//    @GetMapping("/id")
//    public Passport findOne(@RequestParam("id") Long id) {
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
//                    response = Passport.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<Passport> findLimitOffset(@RequestParam("limit") Long limit,
//                                          @RequestParam("offset") Long offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of Passport`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = Passport.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<Passport> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of Passport`s entities for update",
//                    required = true)
//            @RequestBody List<Passport> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one Passport`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = Passport.class)
//    })
//    @PostMapping("/post")
//    public Passport saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody Passport entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update Passport`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = Passport.class)
//    })
//    @PutMapping("/put")
//    public Passport updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody Passport entity) {
//        return repository.updateOne(entity);
//    }
//}
