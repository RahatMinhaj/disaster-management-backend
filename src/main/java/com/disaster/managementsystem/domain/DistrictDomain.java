package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.DistrictDto;
import com.disaster.managementsystem.entity.District;
import com.disaster.managementsystem.mapper.DistrictMapper;
import com.disaster.managementsystem.param.DistrictParam;
import com.disaster.managementsystem.repository.DistrictRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DistrictDomain extends BaseDomain<District, UUID> {
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;
    private final DivisionDomain divisionDomain;


    @Autowired
    public DistrictDomain(CustomRepository<District, UUID> repository, DistrictRepository districtRepository, DistrictMapper districtMapper, DivisionDomain divisionDomain) {
        super(repository);
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
        this.divisionDomain = divisionDomain;
    }

    public Optional<DistrictDto> getById(UUID id) {
        Optional<District> district = findById(id);
        return Optional.ofNullable(districtMapper.entityToDto(district.orElse(null)));
    }

    public Optional<District> getEntityById(UUID id) {
        return findById(id);
    }

    @Transactional
    public Optional<DistrictDto> create(DistrictParam param) throws Exception {
        DistrictDto crisisDto = entityToDto(createReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public Optional<DistrictDto> update(DistrictParam param) throws Exception {
        DistrictDto crisisDto = entityToDto(updateReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private District updateReturnEntity(DistrictParam param) throws Exception {
        Optional<District> district = findById(param.getId());
        if (district.isEmpty()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(param, district.get()));
    }

    @Transactional
    private District createReturnEntity(DistrictParam param) throws Exception {
        District district = paramToEntity(param, new District());
        return createByEntity(district);
    }

    public List<DistrictDto> getAll(Specification<District> specification, Sort sort) {
        return districtRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<DistrictDto> getAll(Specification<District> specification, Pageable pageable) {
        return districtRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private DistrictDto entityToDto(District crisis) {
        DistrictDto crisisDto = districtMapper.entityToDto(crisis);
        return crisisDto;
    }

    private District paramToEntity(DistrictParam param, District district) {
        districtMapper.paramToEntity(param, district);
        if (Objects.nonNull(param.getDivisionId())) {
            divisionDomain.getEntityById(param.getDivisionId()).ifPresent(district::setDivision);
        }
        return district;
    }
}