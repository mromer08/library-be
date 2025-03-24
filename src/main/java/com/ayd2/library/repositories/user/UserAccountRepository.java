package com.ayd2.library.repositories.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.user.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmail(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByCui(Long cui);
    boolean existsByCuiAndIdNot(Long cui, UUID id);
}
