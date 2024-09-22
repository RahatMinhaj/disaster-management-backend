package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataAlreadyExistsException;
import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.config.security.JwtService;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.UserDto;
import com.disaster.managementsystem.dto.auth.JwtResponse;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.enums.UserType;
import com.disaster.managementsystem.mapper.UserMapper;
import com.disaster.managementsystem.param.LoginParam;
import com.disaster.managementsystem.param.UserParam;
import com.disaster.managementsystem.repository.UserRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthDomain  extends BaseDomain<User, UUID> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    protected AuthDomain(CustomRepository<User, UUID> repository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, UserMapper userMapper) {
        super(repository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public Optional<UserDto> register(UserParam param) {
        Optional<User> byUserNameIgnoreCase = userRepository.findByUserNameIgnoreCase(param.getUserName());
        if (byUserNameIgnoreCase.isPresent()) {
            throw new DataAlreadyExistsException(getMessage("user.already.exist.by.the.username"));
        }
        User user = paramToEntity(param, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.JUST_AN_USER);
        user = userRepository.save(user);
        /*        var jwtToken = jwtService.generateToken(user);
        log.info("token {}", jwtToken);*/
        return Optional.ofNullable(entityToDto(user));
    }

    public Optional<UserDto> registerFromSeed(User user) {
        Optional<User> byUserNameIgnoreCase = userRepository.findByUserNameIgnoreCase(user.getUsername());
        if (byUserNameIgnoreCase.isPresent()) {
            throw new DataAlreadyExistsException(getMessage("user.already.exist.by.the.username"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.JUST_AN_USER);
        user = userRepository.save(user);
        /*        var jwtToken = jwtService.generateToken(user);
        log.info("token {}", jwtToken);*/
        return Optional.ofNullable(entityToDto(user));
    }

    public JwtResponse login(LoginParam loginParam) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword()));
            User user = userRepository.findByUserNameIgnoreCase(loginParam.getUserName()).orElseThrow(() -> new DataNotFoundException(getMessage("username.not.found")));
            var jwtToken = jwtService.generateToken(user);
            return JwtResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            throw e;
        }
    }


    private UserDto entityToDto(User entity) {
        UserDto userDto = userMapper.entityToDto(entity);
        return userDto;
    }

    private User paramToEntity(UserParam param, User User) {
        userMapper.paramToEntity(param, User);
        return User;
    }

}