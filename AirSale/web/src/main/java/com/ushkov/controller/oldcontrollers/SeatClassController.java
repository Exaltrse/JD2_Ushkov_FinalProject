//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.SeatClass;
//import com.ushkov.repository.imlp.SeatClassRepository;
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
//@Api(tags = "SeatClass", value="The SeatClass API")
//@RestController
//@RequestMapping("/seatclass")
//@RequiredArgsConstructor
//public class SeatClassController {
//
//    private final SeatClassRepository repository;
//
//    @ApiOperation(  value = "Find all SeatClass`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = SeatClass.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<SeatClass> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find SeatClass`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of SeatClass`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = SeatClass.class)
//    })
//    @GetMapping("/id")
//    public SeatClass findOne(@RequestParam("id") Short id) {
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
//                    response = SeatClass.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<SeatClass> findLimitOffset(@RequestParam("limit") Short limit,
//                                           @RequestParam("offset") Short offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of SeatClass`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = SeatClass.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<SeatClass> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of SeatClass`s entities for update",
//                    required = true)
//            @RequestBody List<SeatClass> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one SeatClass`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = SeatClass.class)
//    })
//    @PostMapping("/post")
//    public SeatClass saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody SeatClass entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update SeatClass`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = SeatClass.class)
//    })
//    @PutMapping("/put")
//    public SeatClass updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody SeatClass entity) {
//        return repository.updateOne(entity);
//    }
//}
