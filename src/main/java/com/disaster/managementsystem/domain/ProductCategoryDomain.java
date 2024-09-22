package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.ProductCategoryDto;
import com.disaster.managementsystem.entity.ProductCategory;
import com.disaster.managementsystem.mapper.ProductCategoryMapper;
import com.disaster.managementsystem.param.ProductCategoryParam;
import com.disaster.managementsystem.repository.ProductCategoryRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ProductCategoryDomain extends BaseDomain<ProductCategory, UUID> {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    protected ProductCategoryDomain(CustomRepository<ProductCategory, UUID> repository, ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        super(repository);
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    public Optional<ProductCategoryDto> getById(UUID id) {
        Optional<ProductCategory> productCategory = findById(id);
        return Optional.ofNullable(entityToDto(productCategory.orElse(null)));
    }

    @Transactional
    public Optional<ProductCategoryDto> create(ProductCategoryParam param) throws Exception {
        ProductCategory returnEntity = createReturnEntity(param);
        return Optional.ofNullable(entityToDto(returnEntity));
    }

    @Transactional
    public Optional<ProductCategoryDto> update(ProductCategoryParam param) throws Exception {
        return Optional.ofNullable(entityToDto(updateReturnEntity(param)));
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private ProductCategory updateReturnEntity(ProductCategoryParam param) throws Exception {
        Optional<ProductCategory> productCategory = findById(param.getId());
        if (productCategory.isEmpty()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(param, productCategory.get()));
    }


    @Transactional
    public ProductCategory createReturnEntity(ProductCategoryParam param) throws Exception {
        ProductCategory productCategory = paramToEntity(param, new ProductCategory());
        return createByEntity(productCategory);
    }

    public List<ProductCategoryDto> getAll(Specification<ProductCategory> specification, Sort sort) {
        return productCategoryRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<ProductCategoryDto> getAll(Specification<ProductCategory> specification, Pageable pageable) {
        return productCategoryRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ProductCategoryDto entityToDto(ProductCategory entity) {
        ProductCategoryDto productCategoryDto = productCategoryMapper.entityToDto(entity);
        return productCategoryDto;
    }

    private ProductCategory paramToEntity(ProductCategoryParam param, ProductCategory entity) throws Exception {
        productCategoryMapper.paramToEntity(param, entity);
        return entity;
    }

}
