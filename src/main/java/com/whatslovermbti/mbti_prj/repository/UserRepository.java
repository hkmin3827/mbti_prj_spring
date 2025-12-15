package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.Provider;
import com.whatslovermbti.mbti_prj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndOauthId(
            Provider provider,
            String oauthId
    );

    Optional<User> findById(Long Id);
}