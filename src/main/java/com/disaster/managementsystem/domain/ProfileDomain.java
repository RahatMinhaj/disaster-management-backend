package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.component.error.exception.FailedException;
import com.disaster.managementsystem.component.error.exception.NotAllowedException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.ProfileDto;
import com.disaster.managementsystem.entity.Profile;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.enums.Status;
import com.disaster.managementsystem.enums.UserType;
import com.disaster.managementsystem.mapper.ProfileMapper;
import com.disaster.managementsystem.param.CreateProfileByAdminParam;
import com.disaster.managementsystem.param.ProfileParam;
import com.disaster.managementsystem.repository.ProfileRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class ProfileDomain extends BaseDomain<Profile, UUID> {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserDomain userDomain;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public ProfileDomain(CustomRepository<Profile, UUID> repository, ProfileRepository profileRepository, ProfileMapper profileMapper, UserDomain userDomain, PasswordEncoder passwordEncoder) {
        super(repository);
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.userDomain = userDomain;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ProfileDto> getById(UUID id) {
        Optional<Profile> jobSeeker = findById(id);
        return Optional.ofNullable(entityToDto(jobSeeker.orElse(null)));
    }

    @Transactional
    public Optional<ProfileDto> create(ProfileParam param) throws Exception {
        ProfileDto profileDto = entityToDto(createReturnEntity(param));
        return Optional.of(profileDto);
    }


    @Transactional
    private Profile createReturnEntity(ProfileParam param) throws Exception {
        Profile profile = paramToEntity(param, new Profile());
        User user = null;
        if (Objects.nonNull(param.getUserId())) {
            user = userDomain.findById(param.getUserId()).orElseThrow(() -> new DataNotFoundException(getMessage("user.is.not.created.yet")));
            if (Objects.nonNull(user.getProfile())) {
                throw new NotAllowedException(getMessage("profile.already.created"));
            }
            profile.setUser(user);
        }
        profile.setStatus(Status.SUBMITTED);
        profile = createByEntity(profile);
        assert user != null;
        user.setProfile(profile);
        user.setProfileCreated(true);
        user.setUserType(UserType.VOLUNTEER);
        if (Objects.nonNull(profile)) userDomain.updateByEntity(user);
        return profile;
    }

    public List<ProfileDto> getAll(Specification<Profile> specification, Sort sort) {
        return profileRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<ProfileDto> getAll(Specification<Profile> specification, Pageable pageable) {
        return profileRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ProfileDto entityToDto(Profile profile) {
        ProfileDto profileDto = profileMapper.entityToDto(profile);
        return profileDto;
    }

    private Profile paramToEntity(ProfileParam param, Profile profile) {
        profileMapper.paramToEntity(param, profile);
        return profile;
    }

    @Transactional
    public ProfileDto approveVolunteer(UUID profileId) throws Exception {
        Profile profile = findById(profileId).orElseThrow(() -> new DataNotFoundException(getMessage("data.not.found")));
        profile.setStatus(Status.APPROVED);
        return entityToDto(updateByEntity(profile));
    }

    @Transactional
    public ProfileDto rejectVolunteer(UUID profileId) throws Exception {
        Profile profile = findById(profileId).orElseThrow(() -> new DataNotFoundException(getMessage("data.not.found")));
        profile.setStatus(Status.BANNED);
        return entityToDto(updateByEntity(profile));
    }

    @Transactional
    public ProfileDto createProfileByAdmin(CreateProfileByAdminParam param) throws Exception {
        if (userDomain.existsByUserNameIgnoreCase(param.getUserName())) {
            throw new FailedException(getMessage("user.already.exist.by.the.username"));
        }
        User user = User.builder().email(param.getEmail()).userName(param.getUserName()).password(passwordEncoder.encode(param.getPassword())).profileCreated(true).userType(UserType.VOLUNTEER).build();
        User byEntity = userDomain.createByEntity(user);

        Profile profile = Profile.builder().name(param.getName()).mobileNo(param.getMobileNo()).address(param.getAddress()).genderType(param.getGenderType()).dateOfBirth(param.getDateOfBirth()).user(byEntity).status(Status.APPROVED).build();
        Profile entity = createByEntity(profile);
        byEntity.setProfile(profile);
        return entityToDto(profile);
    }

}