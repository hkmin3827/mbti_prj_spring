package com.whatslovermbti.mbti_prj.domain.user.repository;

import com.whatslovermbti.mbti_prj.global.constant.Provider;
import com.whatslovermbti.mbti_prj.global.constant.Role;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndOauthId(
            Provider provider,
            String oauthId
    );

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByRoleAndNameContainingIgnoreCase(
            Role role,
            String name,
            Pageable pageable
    );

    Page<User> findByRoleAndIsActive(
            Role role,
            boolean isActive,
            Pageable pageable
    );

    Page<User> findByRoleAndIsActiveAndNameContainingIgnoreCase(
            Role role,
            boolean isActive,
            String name,
            Pageable pageable
    );
}
