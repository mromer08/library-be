package com.ayd2.library.security;

import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ayd2.library.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
            HttpHeaders.ORIGIN,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT,
            HttpHeaders.AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "DELETE",
            "PUT",
            "PATCH"
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs*/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/publishers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/publishers/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/publishers").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/publishers/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/publishers/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/authors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/authors/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authors").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/authors/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/authors/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/degrees").permitAll()
                        .requestMatchers(HttpMethod.GET, "/degrees/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/degrees").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/degrees/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/degrees/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/books").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/books/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/books/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/loans/**").hasAnyAuthority("LIBRARIAN", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/loans/{id}").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/loans/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers("/payments/**").hasAuthority("LIBRARIAN")
                        .requestMatchers("/reservations/**").hasAuthority("STUDENT")
                        .requestMatchers("/reports/**").hasAuthority("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/students").permitAll()
                        .requestMatchers(HttpMethod.GET, "/students/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/students/{id}").hasAuthority("LIBRARIAN")

                        .requestMatchers("/ping/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/configuration").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/configuration").hasAuthority("LIBRARIAN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenService, userDetailsService, jwtProperties.getAccess().getSecretKey());
    }
}