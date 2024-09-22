package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.ProductCategoryDomain;
import com.disaster.managementsystem.dto.ProductCategoryDto;
import com.disaster.managementsystem.entity.ProductCategory;
import com.disaster.managementsystem.param.PageableParam;
import com.disaster.managementsystem.param.ProductCategoryParam;
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
@RequestMapping(ApiProvider.ProductCategory.ROOTPATH)
public class ProductCategoryController extends AbstractController implements GetAllApi<ProductCategory>, GetApi<ProductCategoryDto>, CreateApi<ProductCategoryDto, ProductCategoryParam>, UpdateApi<ProductCategoryDto, ProductCategoryParam>, DeleteApi{
    private final ProductCategoryDomain productCategoryDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProductCategoryDto.class)))
    })
    @PostMapping()
    @Override
    public ResponseEntity<ApiResponseDto<ProductCategoryDto>> save(@RequestBody ProductCategoryParam param) throws Exception {
        log.debug("Request to Create organization-designation... {}", param);
        // Return result and message.
        return generateResponse(productCategoryDomain.create(param), HttpStatus.CREATED, "Created Successful!");
    }

    @DeleteMapping(value = ApiProvider.ProductCategory.PRODUCT_CATEGORY_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        productCategoryDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Deleted success");
    }

    @Operation(summary = "Get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProductCategoryDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "simecCVFolderId", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping()
    @Override
    public ResponseEntity findAll(
            @And({
                    @Spec(path = "id", params = "simecCVFolderId", spec = Equal.class),
                    @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "active", params = "active", defaultVal = "true", spec = Equal.class),
            }) @Schema(hidden = true) Specification<ProductCategory> specification, @Schema(hidden = true) PageableParam pageable) {
        if (pageable.isPageable()) {
            return generateResponse(productCategoryDomain.getAll(specification, pageable.getPageable()), HttpStatus.OK, "data fetch success!");
        }
        return generateResponse(productCategoryDomain.getAll(specification, pageable.getSort()), HttpStatus.OK,"data fetch success!" );
    }

    @Operation(summary = "Find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProductCategoryDto.class)))
    })
    @GetMapping(value = ApiProvider.ProductCategory.PRODUCT_CATEGORY_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<ProductCategoryDto>> findById(@PathVariable("id") UUID id) {
        Optional<ProductCategoryDto> simecCVFolderDto = productCategoryDomain.getById(id);
        return generateResponse(simecCVFolderDto, HttpStatus.OK, "Data get Successfully!");
    }

    @Operation(summary = "Update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProductCategoryDto.class)))
    })
    @PutMapping(value = ApiProvider.ProductCategory.PRODUCT_CATEGORY_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<ProductCategoryDto>> update(@PathVariable("id") UUID id, @RequestBody ProductCategoryParam param) throws Exception {
        log.debug("Request to Update organization-designation...{} --> {}", id, param);
        param.setId(id);
        Optional<ProductCategoryDto> simecCVFolderDto = productCategoryDomain.update(param);
        return generateResponse(simecCVFolderDto, HttpStatus.OK, "Update success!");
    }

}
