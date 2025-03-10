package com.example.kadastr.security;

import com.example.kadastr.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

//global security config
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST, "/api/auth/*").permitAll()
                        .requestMatchers(GET, "/api/comments/*", "/api/comments").permitAll()
                        .requestMatchers(GET, "/api/news/*", "/api/news").permitAll()
                        .requestMatchers(POST, "/api/comments").hasAnyRole("ADMIN", "SUBSCRIBER")
                        .requestMatchers(POST, "/api/news").hasAnyRole("ADMIN", "JOURNALIST")
                        .requestMatchers(PATCH, "/api/comments/*").hasAnyRole("ADMIN", "SUBSCRIBER")
                        .requestMatchers(PATCH, "/api/news/*").hasAnyRole("ADMIN", "JOURNALIST")
                        .requestMatchers(DELETE, "/api/comments/*").hasAnyRole("ADMIN", "SUBSCRIBER")
                        .requestMatchers(DELETE, "/api/news/*").hasAnyRole("ADMIN", "JOURNALIST")
                        .anyRequest().hasRole("ADMIN")
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
