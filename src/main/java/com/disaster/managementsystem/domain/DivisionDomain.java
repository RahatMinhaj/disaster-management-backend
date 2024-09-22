package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.DivisionDto;
import com.disaster.managementsystem.entity.Division;
import com.disaster.managementsystem.mapper.DivisionMapper;
import com.disaster.managementsystem.param.DivisionParam;
import com.disaster.managementsystem.repository.DivisionRepository;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DivisionDomain extends BaseDomain<Division, UUID> {
    private final DivisionRepository divisionRepository;
    private final DivisionMapper divisionMapper;


    @Autowired
    public DivisionDomain(CustomRepository<Division, UUID> repository, DivisionRepository divisionRepository, DivisionMapper divisionMapper) {
        super(repository);
        this.divisionRepository = divisionRepository;
        this.divisionMapper = divisionMapper;
    }

    public Optional<DivisionDto> getById(UUID id) {
        Optional<Division> division = findById(id);
        return Optional.ofNullable(divisionMapper.entityToDto(division.orElse(null)));
    }

    public Optional<Division> getEntityById(UUID id) {
        return findById(id);
    }

    @Transactional
    public Optional<DivisionDto> create(DivisionParam param) throws Exception {
        DivisionDto crisisDto = entityToDto(createReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public Optional<DivisionDto> update(DivisionParam param) throws Exception {
        DivisionDto crisisDto = entityToDto(updateReturnEntity(param));
//        saveBranchLogo(crisisDto);
        return Optional.of(crisisDto);
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private Division updateReturnEntity(DivisionParam crisisParam) throws Exception {
        Optional<Division> simecBranchOptional = findById(crisisParam.getId());
        if (!simecBranchOptional.isPresent()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(crisisParam, simecBranchOptional.get()));
    }

    @Transactional
    private Division createReturnEntity(DivisionParam param) throws Exception {
        Division division = paramToEntity(param, new Division());
        return createByEntity(division);
    }

    public List<DivisionDto> getAll(Specification<Division> specification, Sort sort) {
        return divisionRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<DivisionDto> getAll(Specification<Division> specification, Pageable pageable) {
        return divisionRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private DivisionDto entityToDto(Division crisis) {
        DivisionDto crisisDto = divisionMapper.entityToDto(crisis);
//        prepareBranchLogo(crisisDto);
        return crisisDto;
    }

    private Division paramToEntity(DivisionParam param, Division crisis) {
        divisionMapper.paramToEntity(param, crisis);
        return crisis;
    }
}