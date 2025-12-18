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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use custom CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Custom 401 JSON
                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers(HttpMethod.POST, "/api/auth/sign-in").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/restore-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/change-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/customer").permitAll()

                        // Protected
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()

                        // User module
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAuthority("USER_READ")
                        .requestMatchers(HttpMethod.POST, "/api/user/**").hasAuthority("USER_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAuthority("USER_DELETE")

                        // Entity module
                        .requestMatchers(HttpMethod.GET, "/api/system-entities").hasAuthority("ROLE_READ")

                        // Role module
                        .requestMatchers(HttpMethod.GET, "/api/role/active").hasAnyAuthority("USER_CREATE", "USER_UPDATE")                        .requestMatchers(HttpMethod.GET, "/api/role/**").hasAuthority("ROLE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/role/**").hasAuthority("ROLE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/role/**").hasAuthority("ROLE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/role/**").hasAuthority("ROLE_DELETE")

                        // Identification type module
                        .requestMatchers(HttpMethod.GET, "/api/identification-type/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/identification-type/**").hasAuthority("IDENTIFICATION_TYPE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/identification-type/**").hasAuthority("IDENTIFICATION_TYPE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/identification-type/**").hasAuthority("IDENTIFICATION_TYPE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/identification-type/**").hasAuthority("IDENTIFICATION_TYPE_DELETE")

                        // Loan type module
                        .requestMatchers(HttpMethod.GET, "/api/loan-type/active").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/loan-type").hasAuthority("LOAN_TYPE_READ")
                        .requestMatchers(HttpMethod.POST, "/api/loan-type/**").hasAuthority("LOAN_TYPE_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/loan-type/**").hasAuthority("LOAN_TYPE_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/loan-type/**").hasAuthority("LOAN_TYPE_DELETE")

                        // Loan request status module
                        .requestMatchers(HttpMethod.GET, "/api/loan-request-status").hasAnyAuthority("LOAN_REQUEST_READ", "LOAN_REQUEST_UPDATE")

                        // Loan request module
                        .requestMatchers(HttpMethod.POST, "/api/loan-request").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/loan-request").hasAnyAuthority("LOAN_REQUEST_UPDATE")

                        // Everything else requires authentication
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