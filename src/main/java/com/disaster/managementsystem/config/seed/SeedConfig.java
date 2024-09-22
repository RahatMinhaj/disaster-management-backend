package com.disaster.managementsystem.config.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.disaster.managementsystem.domain.AuthDomain;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.enums.UserType;
import com.disaster.managementsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
public class SeedConfig {
    @Bean
    public CommandLineRunner run(UserRepository userRepository, AuthDomain authDomain
    ) {
        return (String[] args) -> {
            try {
                createUsers(userRepository,authDomain);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        };
    }

    public void createUsers(UserRepository userRepository, AuthDomain authDomain){
        try {
            InputStream usersStream = new ClassPathResource("seed/seed_user.json",
                    this.getClass().getClassLoader()).getInputStream();
            String text = new String(usersStream.readAllBytes());
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String>[] usersJson = mapper.readValue(text, Map[].class);

            for (Map<String, String> map : usersJson) {
                try {
                    String userName = map.get("username");
                    String email = map.get("email");
                    String password = map.get("password");
                    if (Objects.nonNull(userName) && Objects.nonNull(email) && Objects.nonNull(password)){
                        if (!userRepository.existsByUserNameIgnoreCaseOrEmailIgnoreCase(userName, email)){
                            User user = User.builder()
                                    .userName(userName)
                                    .email(email)
                                    .password(password)
                                    .userType(UserType.ADMIN)
                                    .profileCreated(false)
                                    .build();
                            authDomain.registerFromSeed(user);
                        }
                    }
                } catch (Exception e) {
                    log.info(e.toString());
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            log.info(e.toString());
            throw new RuntimeException(e);
        }
    }
}