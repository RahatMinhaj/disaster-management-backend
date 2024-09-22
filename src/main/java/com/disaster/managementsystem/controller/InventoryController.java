package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.InventoryDomain;
import com.disaster.managementsystem.dto.InventoryDto;
import com.disaster.managementsystem.entity.Inventory;
import com.disaster.managementsystem.param.InventoryParam;
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
@RequestMapping(ApiProvider.Inventory.ROOTPATH)
public class InventoryController extends AbstractController implements GetApi<InventoryDto>, /*GetAllApi<JobPost>,*/ CreateApi<InventoryDto, InventoryParam>, UpdateApi<InventoryDto, InventoryParam>, DeleteApi {
    private final InventoryDomain inventoryDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = InventoryDto.class)))
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResponseDto<InventoryDto>> save(@RequestBody InventoryParam param) throws Exception {
        log.debug("Request to Create job-post... {}", param);
        // Return result and message.
        return generateResponse(inventoryDomain.create(param), HttpStatus.CREATED, "Data created successfully");
    }

    @Operation(summary = "find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = InventoryDto.class)))
    })
    @GetMapping(value = ApiProvider.Inventory.INVENTORY_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<InventoryDto>> findById(@PathVariable("id") UUID id) {
        Optional<InventoryDto> jobPostOptional = inventoryDomain.getById(id);
        return generateResponse(jobPostOptional, HttpStatus.OK, "Data found");
    }

    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = InventoryDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    public ResponseEntity findAll(
            @And({
                    @Spec(path = "active", params = "active", spec = Equal.class),
            }) @Schema(hidden = true) Specification<Inventory> specification, @Schema(hidden = true) PageableParam pageable) {

        if (pageable.isPageable()) {
            Page<InventoryDto> jobPostDtos = inventoryDomain.getAll(specification, pageable.getPageable());
            return generateResponse(jobPostDtos, HttpStatus.OK, "Data found");
        }
        return generateResponse(inventoryDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = InventoryDto.class)))
    })
    @PutMapping(value = ApiProvider.Inventory.INVENTORY_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<InventoryDto>> update(@PathVariable("id") UUID id, @RequestBody InventoryParam param) throws Exception {
        log.debug("Request to Update job-post...{} --> {}", id, param);
        param.setId(id);
        Optional<InventoryDto> jobPostDtoOptional = inventoryDomain.update(param);
        return generateResponse(jobPostDtoOptional, HttpStatus.OK, "Data updated successfully");
    }

    @Operation(summary = "delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content)
    })
    @DeleteMapping(value = ApiProvider.Inventory.INVENTORY_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        inventoryDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Data deleted successfully");
    }

}
