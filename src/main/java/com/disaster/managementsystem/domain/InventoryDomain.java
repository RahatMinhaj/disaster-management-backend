package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.InventoryDto;
import com.disaster.managementsystem.entity.Inventory;
import com.disaster.managementsystem.mapper.InventoryMapper;
import com.disaster.managementsystem.param.InventoryParam;
import com.disaster.managementsystem.repository.InventoryRepository;
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
public class InventoryDomain extends BaseDomain<Inventory, UUID> {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProfileDomain profileDomain;


    @Autowired
    public InventoryDomain(CustomRepository<Inventory, UUID> repository, InventoryRepository inventoryRepository, InventoryMapper inventoryMapper, ProfileDomain profileDomain) {
        super(repository);
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.profileDomain = profileDomain;
    }

    public Optional<InventoryDto> getById(UUID id) {
        Optional<Inventory> division = findById(id);
        return Optional.ofNullable(inventoryMapper.entityToDto(division.orElse(null)));
    }

    public Optional<Inventory> getEntityById(UUID id) {
        return findById(id);
    }

    @Transactional
    public Optional<InventoryDto> create(InventoryParam param) throws Exception {
        InventoryDto crisisDto = entityToDto(createReturnEntity(param));
        return Optional.of(crisisDto);
    }

    @Transactional
    public Optional<InventoryDto> update(InventoryParam param) throws Exception {
        InventoryDto crisisDto = entityToDto(updateReturnEntity(param));
//        saveBranchLogo(crisisDto);
        return Optional.of(crisisDto);
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private Inventory updateReturnEntity(InventoryParam crisisParam) throws Exception {
        Optional<Inventory> simecBranchOptional = findById(crisisParam.getId());
        if (simecBranchOptional.isEmpty()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(crisisParam, simecBranchOptional.get()));
    }

    @Transactional
    private Inventory createReturnEntity(InventoryParam param) throws Exception {
        Inventory inventory = paramToEntity(param, new Inventory());
        if (Objects.nonNull(param.getProfileId())) {
            profileDomain.findById(param.getProfileId()).ifPresent(inventory::setProfile);
        }

        return createByEntity(inventory);
    }

    public List<InventoryDto> getAll(Specification<Inventory> specification, Sort sort) {
        return inventoryRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<InventoryDto> getAll(Specification<Inventory> specification, Pageable pageable) {
        return inventoryRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private InventoryDto entityToDto(Inventory crisis) {
        InventoryDto crisisDto = inventoryMapper.entityToDto(crisis);
//        prepareBranchLogo(crisisDto);
        return crisisDto;
    }

    private Inventory paramToEntity(InventoryParam param, Inventory crisis) {
        inventoryMapper.paramToEntity(param, crisis);
        return crisis;
    }
}