package com.ushkov.security.service;


import com.ushkov.domain.Users;
import com.ushkov.repository.imlp.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProviderService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<Users> searchResult = Optional.ofNullable(userRepository.findByLogin(username));
            if (searchResult.isPresent()) {
                Users user = searchResult.get();
                return new org.springframework.security.core.userdetails.User(
                        user.getLogin(),
                        user.getPassword(),
                        AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().getName())
                );
            } else {
                throw new UsernameNotFoundException(String.format("No user found with login '%s'.", username));
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User with this login not found");
        }
    }
}
