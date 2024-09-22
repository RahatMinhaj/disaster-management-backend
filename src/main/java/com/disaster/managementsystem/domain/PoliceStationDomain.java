package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.PoliceStationDto;
import com.disaster.managementsystem.entity.PoliceStation;
import com.disaster.managementsystem.mapper.PoliceStationMapper;
import com.disaster.managementsystem.param.PoliceStationParam;
import com.disaster.managementsystem.repository.PoliceStationRepository;
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
public class PoliceStationDomain extends BaseDomain<PoliceStation, UUID> {
    private final PoliceStationRepository policeStationRepository;
    private final PoliceStationMapper policeStationMapper;
    private final DistrictDomain districtDomain;


    @Autowired
    public PoliceStationDomain(CustomRepository<PoliceStation, UUID> repository, PoliceStationRepository policeStationRepository, PoliceStationMapper policeStationMapper, DistrictDomain districtDomain) {
        super(repository);
        this.policeStationRepository = policeStationRepository;
        this.policeStationMapper = policeStationMapper;
        this.districtDomain = districtDomain;
    }

    public Optional<PoliceStationDto> getById(UUID id) {
        Optional<PoliceStation> district = findById(id);
        return Optional.ofNullable(policeStationMapper.entityToDto(district.orElse(null)));
    }

    public Optional<PoliceStation> getEntityById(UUID id) {
        return findById(id);
    }

    @Transactional
    public Optional<PoliceStationDto> create(PoliceStationParam param) throws Exception {
        PoliceStationDto crisisDto = entityToDto(createReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public Optional<PoliceStationDto> update(PoliceStationParam param) throws Exception {
        PoliceStationDto crisisDto = entityToDto(updateReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private PoliceStation updateReturnEntity(PoliceStationParam param) throws Exception {
        Optional<PoliceStation> simecBranchOptional = findById(param.getId());
        if (!simecBranchOptional.isPresent()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(param, simecBranchOptional.get()));
    }

    @Transactional
    private PoliceStation createReturnEntity(PoliceStationParam param) throws Exception {
        PoliceStation district = paramToEntity(param, new PoliceStation());
        return createByEntity(district);
    }

    public List<PoliceStationDto> getAll(Specification<PoliceStation> specification, Sort sort) {
        return policeStationRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<PoliceStationDto> getAll(Specification<PoliceStation> specification, Pageable pageable) {
        return policeStationRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private PoliceStationDto entityToDto(PoliceStation crisis) {
        PoliceStationDto crisisDto = policeStationMapper.entityToDto(crisis);
        return crisisDto;
    }

    private PoliceStation paramToEntity(PoliceStationParam param, PoliceStation entity) {
        policeStationMapper.paramToEntity(param, entity);
        if (Objects.nonNull(param.getDistrictId())) {
            districtDomain.findById(param.getDistrictId()).ifPresent(entity::setDistrict);
        }
        return entity;
    }
}