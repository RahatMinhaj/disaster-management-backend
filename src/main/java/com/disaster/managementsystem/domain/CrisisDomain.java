package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.CrisisDto;
import com.disaster.managementsystem.entity.Crisis;
import com.disaster.managementsystem.entity.Profile;
import com.disaster.managementsystem.enums.UserType;
import com.disaster.managementsystem.mapper.CrisisMapper;
import com.disaster.managementsystem.param.CrisisParam;
import com.disaster.managementsystem.param.VolunteerAssignmentParam;
import com.disaster.managementsystem.repository.CrisisRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import com.disaster.managementsystem.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CrisisDomain extends BaseDomain<Crisis, UUID> {
    private final CrisisRepository crisisRepository;
    private final CrisisMapper crisisMapper;
    private final ProfileDomain profileDomain;


    @Autowired
    public CrisisDomain(CustomRepository<Crisis, UUID> repository, CrisisRepository crisisRepository, CrisisMapper crisisMapper, ProfileDomain profileDomain) {
        super(repository);
        this.crisisRepository = crisisRepository;
        this.crisisMapper = crisisMapper;
        this.profileDomain = profileDomain;
    }

    public Optional<CrisisDto> getById(UUID id) {
        Optional<Crisis> optionalSimecBranch = findById(id);
        return Optional.ofNullable(crisisMapper.entityToDto(optionalSimecBranch.orElse(null)));
    }

    public Optional<Crisis> getEntityById(UUID id) {
        Optional<Crisis> optionalSimecBranch = findById(id);
        return Optional.ofNullable(optionalSimecBranch.orElse(null));
    }

    @Transactional
    public Optional<CrisisDto> create(CrisisParam param) throws Exception {
        CrisisDto crisisDto = entityToDto(createReturnEntity(param));
//        saveBranchLogo(crisisDto);
        return Optional.of(crisisDto);
    }

    @Transactional
    public Optional<CrisisDto> update(CrisisParam param) throws Exception {
        CrisisDto crisisDto = entityToDto(updateReturnEntity(param));
//        saveBranchLogo(crisisDto);
        return Optional.of(crisisDto);
    }

    //    Need to work on voluteer self request to the crisis
    @Transactional
    public Optional<CrisisDto> assignVolunteersToTheCrisis(UUID id, VolunteerAssignmentParam param) throws Exception {
        Crisis crisis = findById(id).orElseThrow(() -> new DataNotFoundException(getMessage("data.not.found")));
        Set<Profile> profiles = crisis.getProfiles();
        if (Objects.nonNull(param)) {
            param.getVolunteerIds().forEach(volunteerId -> {
                profileDomain.findById(volunteerId).ifPresent(profile -> {
                    if (profile.getUser().getUserType().equals(UserType.VOLUNTEER)) {
                        profiles.add(profile);
                    }
                });
            });
            crisis.setProfiles(profiles);
            updateByEntity(crisis);
        }
        return Optional.of(entityToDto(updateByEntity(crisis)));
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private Crisis updateReturnEntity(CrisisParam crisisParam) throws Exception {
        Optional<Crisis> simecBranchOptional = findById(crisisParam.getId());
        if (!simecBranchOptional.isPresent()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(crisisParam, simecBranchOptional.get()));
    }

    @Transactional
    private Crisis createReturnEntity(CrisisParam param) throws Exception {
        Crisis crisis = paramToEntity(param, new Crisis());
        crisis.setEntryTime(LocalDateTime.now());
        return createByEntity(crisis);
    }

    public List<CrisisDto> getAll(Specification<Crisis> specification, Sort sort) {
        return crisisRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<CrisisDto> getAll(Specification<Crisis> specification, Pageable pageable) {
        return crisisRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CrisisDto entityToDto(Crisis crisis) {
        CrisisDto crisisDto = crisisMapper.entityToDto(crisis);
//        prepareBranchLogo(crisisDto);
        return crisisDto;
    }

    private Crisis paramToEntity(CrisisParam param, Crisis crisis) {
        crisisMapper.paramToEntity(param, crisis);
        return crisis;
    }

/*    private void saveBranchLogo(CrisisDto crisisDto) {
        if (Objects.nonNull(crisisDto.getLogoPath())) {
            FileMetaDataDto fileMetaDataDto = fileDomain.finalSave(DomainType.ORGANIZATION_LOGO, crisisDto.getLogoPath());
            crisisDto.setLogoDto(fileMetaDataDto);
        }
    }

    private void prepareBranchLogo(CrisisDto crisisDto) {
        if (Objects.nonNull(crisisDto.getLogoPath())) {
            FileMetaDataDto fileMetaDataDto = fileDomain.getFileByFileNameAndDomainType(DomainType.ORGANIZATION_LOGO, crisisDto.getLogoPath());
            if (Objects.nonNull(fileMetaDataDto)) {
                crisisDto.setLogoDto(fileMetaDataDto);
            }
        }
    }*/

    @Transactional
    public Optional<CrisisDto> requestOnCrisis(UUID id, VolunteerAssignmentParam param) throws Exception {

        CommonUtils.getCurrentUserId();


        Crisis crisis = findById(id).orElseThrow(() -> new DataNotFoundException(getMessage("data.not.found")));
//        Set<SelfAssignmentRequest> selfAssignmentRequests = crisis.getSelfAssignmentRequests();

//        selfAssignmentRequests.forEach(selfAssignmentRequest -> {
//            if (id.equals(selfAssignmentRequest.ge))
//        });

        if (Objects.nonNull(param)) {
            Set<Profile> volunteers = new HashSet<>();
            param.getVolunteerIds().forEach(volunteerId -> {
                profileDomain.findById(volunteerId).ifPresent(profile -> {
                    if (profile.getUser().getUserType().equals(UserType.VOLUNTEER)) {
                        volunteers.add(profile);
                    }
                });
            });
            crisis.setProfiles(volunteers);
            updateByEntity(crisis);
        }
        return Optional.of(entityToDto(updateByEntity(crisis)));
    }
}