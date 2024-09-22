package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.MeDto;
import com.disaster.managementsystem.dto.ProfileDto;
import com.disaster.managementsystem.dto.UserDto;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.mapper.UserMapper;
import com.disaster.managementsystem.param.UserParam;
import com.disaster.managementsystem.repository.UserRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import com.disaster.managementsystem.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDomain extends BaseDomain<User, UUID> {
    private final UserRepository userRepository;
    private final AuthDomain authDomain;
    private final UserMapper userMapper;
    private final ProfileDomain profileDomain;
    @Autowired
    protected UserDomain(CustomRepository<User, UUID> repository, UserRepository userRepository, AuthDomain authDomain, UserMapper userMapper, @Lazy ProfileDomain profileDomain) {
        super(repository);
        this.userRepository = userRepository;
        this.authDomain = authDomain;
        this.userMapper = userMapper;
        this.profileDomain = profileDomain;
    }

    public Optional<MeDto> me() {
        User userDetails = CommonUtils.getLoggedInUserDetails().orElse(null);
        MeDto meDto = new MeDto();
        if (Objects.nonNull(userDetails)) {
            if (Objects.nonNull(userDetails.getProfile())){
                ProfileDto profileDto = profileDomain.entityToDto(userDetails.getProfile());
                if (Objects.nonNull(profileDto)) {
                    meDto.setProfileDto(profileDto);
                }
            }
            meDto.setUserId(userDetails.getId());
            meDto.setUserName(userDetails.getUsername());
            meDto.setEmail(userDetails.getEmail());
            meDto.setUserType(userDetails.getUserType());
            meDto.setProfileCreated(userDetails.isProfileCreated());
            return Optional.of(meDto);
        }
        return Optional.empty();
    }


    public List<UserDto> getAll(Specification<User> specification, Sort sort) {
        return userRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<UserDto> getAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private UserDto entityToDto(User user) {
        UserDto userDto = userMapper.entityToDto(user);
        return userDto;
    }

    private User paramToEntity(UserParam param, User user) {
        userMapper.paramToEntity(param, user);
        return user;
    }

    public boolean existsByUserNameIgnoreCase(String userName){
        return userRepository.existsByUserNameIgnoreCase(userName);
    }

}