package com.cotrafa.creditapproval.auth.infrastructure.security;

import com.cotrafa.creditapproval.role.domain.model.Permission;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import com.cotrafa.creditapproval.systementity.domain.port.out.SystemEntityRepositoryPort;
import com.cotrafa.creditapproval.user.application.service.UserApplicationService;
import com.cotrafa.creditapproval.user.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserApplicationService userApplicationService;
    private final RoleRepositoryPort roleRepository;
    private final SystemEntityRepositoryPort systemEntityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        if (!jwtUtil.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenType = jwtUtil.getTokenType(jwt);
        if (!"ACCESS".equalsIgnoreCase(tokenType)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UUID userId = jwtUtil.getUserId(jwt);
                User userDomain = this.userApplicationService.getById(userId);
                UUID tokenSessionId = jwtUtil.getSessionId(jwt);

                if (userDomain.getSessionId() == null || !userDomain.getSessionId().equals(tokenSessionId)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Role userRole = null;
                if (userDomain.getRoleId() != null) {
                    userRole = roleRepository.findById(userDomain.getRoleId())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                }

                List<SystemEntity> allSystemEntities = systemEntityRepository.findAllOrdered();
                Map<UUID, String> entityIdToNameMap = allSystemEntities.stream()
                        .collect(Collectors.toMap(
                                SystemEntity::getId,
                                SystemEntity::getName
                        ));

                UserDetails userDetails = buildUserDetails(userDomain, userRole, entityIdToNameMap);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (ResourceNotFoundException e) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private UserDetails buildUserDetails(User user, Role role, Map<UUID, String> entityIdToNameMap) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(mapPermissionsToAuthorities(role, entityIdToNameMap))
                .build();
    }

    private List<GrantedAuthority> mapPermissionsToAuthorities(Role role, Map<UUID, String> entityIdToNameMap) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            System.out.println(role.getPermissions().stream().toList());

            if (role.getPermissions() != null) {
                for (Permission perm : role.getPermissions()) {
                    if(perm.getEntityId() != null) {

                        String entityName = entityIdToNameMap.get(perm.getEntityId());

                        if (entityName != null) {
                            String entityNameUpper = entityName.toUpperCase();

                            if (perm.isCanRead()) authorities.add(new SimpleGrantedAuthority(entityNameUpper + "_READ"));
                            if (perm.isCanCreate()) authorities.add(new SimpleGrantedAuthority(entityNameUpper + "_CREATE"));
                            if (perm.isCanUpdate()) authorities.add(new SimpleGrantedAuthority(entityNameUpper + "_UPDATE"));
                            if (perm.isCanDelete()) authorities.add(new SimpleGrantedAuthority(entityNameUpper + "_DELETE"));
                        }
                    }
                }
            }
        }
        return authorities;
    }
}