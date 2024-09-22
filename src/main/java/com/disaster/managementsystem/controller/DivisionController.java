package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.DivisionDomain;
import com.disaster.managementsystem.dto.DivisionDto;
import com.disaster.managementsystem.entity.Division;
import com.disaster.managementsystem.param.DivisionParam;
import com.disaster.managementsystem.param.PageableParam;
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
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.Division.ROOTPATH)
public class DivisionController extends AbstractController implements GetApi<DivisionDto>, /*GetAllApi<JobPost>,*/ CreateApi<DivisionDto, DivisionParam>, UpdateApi<DivisionDto, DivisionParam>, DeleteApi {
    private final DivisionDomain divisionDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DivisionDto.class)))
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResponseDto<DivisionDto>> save(@RequestBody DivisionParam param) throws Exception {
        log.debug("Request to Create job-post... {}", param);
        // Return result and message.
        return generateResponse(divisionDomain.create(param), HttpStatus.CREATED, "Data created successfully");
    }

    @Operation(summary = "find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = DivisionDto.class)))
    })
    @GetMapping(value = ApiProvider.Division.DIVISION_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DivisionDto>> findById(@PathVariable("id") UUID id) {
        Optional<DivisionDto> jobPostOptional = divisionDomain.getById(id);
        return generateResponse(jobPostOptional, HttpStatus.OK, "Data found");
    }

    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DivisionDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "geoCode", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "latitude", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "longitude", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    public ResponseEntity findAll(
            @And({
                    @Spec(path = "longitude", params = "longitude", spec = LikeIgnoreCase.class),
                    @Spec(path = "latitude", params = "latitude", spec = LikeIgnoreCase.class),
                    @Spec(path = "geoCode", params = "geoCode", spec = LikeIgnoreCase.class),
                    @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "active", params = "active", spec = Equal.class),
            }) @Schema(hidden = true) Specification<Division> specification, @Schema(hidden = true) PageableParam pageable) {

        if (pageable.isPageable()) {
            Page<DivisionDto> jobPostDtos = divisionDomain.getAll(specification, pageable.getPageable());
            return generateResponse(jobPostDtos, HttpStatus.OK, "Data found");
        }
        return generateResponse(divisionDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = DivisionDto.class)))
    })
    @PutMapping(value = ApiProvider.Division.DIVISION_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DivisionDto>> update(@PathVariable("id") UUID id, @RequestBody DivisionParam param) throws Exception {
        log.debug("Request to Update job-post...{} --> {}", id, param);
        param.setId(id);
        Optional<DivisionDto> jobPostDtoOptional = divisionDomain.update(param);
        return generateResponse(jobPostDtoOptional, HttpStatus.OK, "Data updated successfully");
    }

    @Operation(summary = "delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content)
    })
    @DeleteMapping(value = ApiProvider.Division.DIVISION_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        divisionDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Data deleted successfully");
    }

}
