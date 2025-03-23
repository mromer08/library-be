package com.ayd2.library.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ayd2.library.models.user.Role;
import com.ayd2.library.models.user.UserAccount;
import com.ayd2.library.repositories.user.UserAccountRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new User(user.getEmail(), user.getPassword(), mapRoleToAuthority(user.getRole()));
    }

    private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
    }
}