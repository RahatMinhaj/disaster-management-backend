package com.disaster.managementsystem.config.security;

import com.disaster.managementsystem.component.error.UnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UnauthorizedEntryPoint unauthorizedEntryPoint) throws Exception{
        httpSecurity
                .csrf((csrf) -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/crisis", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/crisis", HttpMethod.POST.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/divisions", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/divisions", HttpMethod.POST.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/job-posts/**", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/job-posts",HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/job-seekers", HttpMethod.POST.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/dashboards", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/file/**", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/file/upload", HttpMethod.POST.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/login", HttpMethod.POST.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name())).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/webjars/**", HttpMethod.GET.name())).permitAll()
                            .anyRequest().permitAll();
//                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(unauthorizedEntryPoint);
                })
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    return configuration;
                }));
        return httpSecurity.build();
    }
}