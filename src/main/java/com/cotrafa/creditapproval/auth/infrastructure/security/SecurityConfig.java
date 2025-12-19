package com.cotrafa.creditapproval.auth.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        // --- PUBLIC ENDPOINTS ---
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/sign-in",
                                "/api/v1/auth/restore-password",
                                "/api/v1/auth/change-password",
                                "/api/v1/auth/refresh-token",
                                "/api/v1/customer").permitAll()

                        // Public endpoint for the registration form to load document types
                        .requestMatchers(HttpMethod.GET, "/api/v1/identification-type/active").permitAll()

                        // --- PROTECTED AUTH ---
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").authenticated()

                        // --- USER MODULE ---
                        // List and search by ID
                        .requestMatchers(HttpMethod.GET, "/api/v1/user", "/api/v1/user/*").hasAuthority("USER_READ")
                        .requestMatchers(HttpMethod.POST, "/api/v1/user").hasAuthority("USER_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/*").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/user/*/reset-password").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/*").hasAuthority("USER_DELETE")

                        // --- ROLE MODULE ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/role/active").hasAnyAuthority("USER_CREATE", "USER_UPDATE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/role", "/api/v1/role/*").hasAuthority("ROLE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/v1/role").hasAuthority("ROLE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/role/*").hasAuthority("ROLE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/role/*").hasAuthority("ROLE_DELETE")

                        // --- IDENTIFICATION TYPE MODULE ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/identification-type").hasAuthority("IDENTIFICATION_TYPE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/v1/identification-type").hasAuthority("IDENTIFICATION_TYPE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/identification-type/*").hasAuthority("IDENTIFICATION_TYPE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/identification-type/*").hasAuthority("IDENTIFICATION_TYPE_DELETE")

                        // --- LOAN TYPE MODULE ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/loan-type/active").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/loan-type").hasAuthority("LOAN_TYPE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/v1/loan-type").hasAuthority("LOAN_TYPE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/loan-type/*").hasAuthority("LOAN_TYPE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/loan-type/*").hasAuthority("LOAN_TYPE_DELETE")

                        // --- LOAN REQUEST MODULE ---
                        // An authenticated customer creates their request
                        .requestMatchers(HttpMethod.POST, "/api/v1/loan-request").authenticated()
                        // Only analysts read the list or the details
                        .requestMatchers(HttpMethod.GET, "/api/v1/loan-request", "/api/v1/loan-request/*").hasAuthority("LOAN_REQUEST_READ")
                        // Only analysts change the state
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/loan-request/*/status").hasAuthority("LOAN_REQUEST_UPDATE")

                        // State master for frontend selects
                        .requestMatchers(HttpMethod.GET, "/api/v1/loan-request-status").hasAnyAuthority("LOAN_REQUEST_READ", "LOAN_REQUEST_UPDATE")

                        // --- SYSTEM ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/system-entities").hasAuthority("ROLE_READ")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000")); // Frontend URLs
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}