package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.CrisisDomain;
import com.disaster.managementsystem.dto.CrisisDto;
import com.disaster.managementsystem.entity.Crisis;
import com.disaster.managementsystem.param.CrisisParam;
import com.disaster.managementsystem.param.PageableParam;
import com.disaster.managementsystem.param.VolunteerAssignmentParam;
import com.disaster.managementsystem.support.ApiResponseDto;
import com.disaster.managementsystem.support.DeleteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.Crisis.ROOTPATH)
public class CrisisController extends AbstractController implements GetAllApi<Crisis>, GetApi<CrisisDto>, CreateApi<CrisisDto, CrisisParam>, UpdateApi<CrisisDto, CrisisParam>, DeleteApi{

    private final CrisisDomain crisisDomain;


    @Operation(summary = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = CrisisDto.class)))
    })
    @PostMapping()
    @Override
    public ResponseEntity<ApiResponseDto<CrisisDto>> save(@RequestBody CrisisParam param) throws Exception {
        log.debug("Request to Create organization-designation... {}", param);
        // Return result and message.
        return generateResponse(crisisDomain.create(param), HttpStatus.CREATED, "Created Successful!");
    }

    @DeleteMapping(value = ApiProvider.Crisis.CRISIS_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        crisisDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Deleted success");
    }

    @Override
    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = CrisisDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "id", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    public ResponseEntity findAll(
            @And({
            @Spec(path = "simecBranchId", params = "id", spec = Equal.class),
            @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "active", params = "active", defaultVal = "true", spec = Equal.class),
    }) @Schema(hidden = true) Specification<Crisis> specification, @Schema(hidden = true) PageableParam pageable) {
        if (pageable.isPageable()) {
            return generateResponse(crisisDomain.getAll(specification, pageable.getPageable()), HttpStatus.OK, "data fetch success!");
        }
        return generateResponse(crisisDomain.getAll(specification, pageable.getSort()), HttpStatus.OK,"data fetch success!" );
    }


    @Operation(summary = "find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = CrisisDto.class)))
    })
    @GetMapping(value = ApiProvider.Crisis.CRISIS_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<CrisisDto>> findById(@PathVariable("id") UUID id) {
        Optional<CrisisDto> organizationDesignationDto = crisisDomain.getById(id);
        return generateResponse(organizationDesignationDto, HttpStatus.OK, "Data get Successfully!");
    }




    @Operation(summary = "update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = CrisisDto.class)))
    })
    @PutMapping(value = ApiProvider.Crisis.CRISIS_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<CrisisDto>> update(@PathVariable("id") UUID id, @RequestBody CrisisParam param) throws Exception {
        log.debug("Request to Update organization-designation...{} --> {}", id, param);
        param.setId(id);
        Optional<CrisisDto> organizationDesignationDto = crisisDomain.update(param);
        return generateResponse(organizationDesignationDto, HttpStatus.OK, "Update success!");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = CrisisDto.class)))
    })
    @PutMapping(value = ApiProvider.Crisis.VOLUNTEER_ASSIGNMENT)
    public ResponseEntity<ApiResponseDto<CrisisDto>> assignVolunteersToTheCrisis(@PathVariable("id") UUID id, @RequestBody VolunteerAssignmentParam param) throws Exception {
        Optional<CrisisDto> organizationDesignationDto = crisisDomain.assignVolunteersToTheCrisis(id, param);
        return generateResponse(organizationDesignationDto, HttpStatus.OK, "Update success!");
    }
}