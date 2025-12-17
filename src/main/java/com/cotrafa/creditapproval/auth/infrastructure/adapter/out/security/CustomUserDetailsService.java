package com.cotrafa.creditapproval.auth.infrastructure.adapter.out.security;

import com.cotrafa.creditapproval.role.domain.model.Permission;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import com.cotrafa.creditapproval.systementity.domain.port.out.SystemEntityRepositoryPort;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final SystemEntityRepositoryPort systemEntityRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = fetchRole(user.getRoleId());

        Map<UUID, String> entityIdToNameMap = fetchSystemEntityNames();

        return buildSpringUser(user, role, entityIdToNameMap);
    }

    private Role fetchRole(UUID roleId) {
        if (roleId == null) return null;
        return roleRepositoryPort.findById(roleId).orElse(null);
    }

    private Map<UUID, String> fetchSystemEntityNames() {
        List<SystemEntity> allSystemEntities = systemEntityRepositoryPort.findAllOrdered();
        return allSystemEntities.stream()
                .collect(Collectors.toMap(
                        SystemEntity::getId,
                        SystemEntity::getName
                ));
    }

    private UserDetails buildSpringUser(User user, Role role, Map<UUID, String> entityIdToNameMap) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            if (role.getPermissions() != null) {
                for (Permission perm : role.getPermissions()) {

                    if (perm.getEntityId() != null) {

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

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.isActive())
                .accountLocked(user.isLocked())
                .build();
    }
}