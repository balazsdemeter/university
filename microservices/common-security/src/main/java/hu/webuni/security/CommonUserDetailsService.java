package hu.webuni.security;

import hu.webuni.model.SecurityUser;
import hu.webuni.repository.SecurityUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class CommonUserDetailsService implements UserDetailsService {

    private final SecurityUserRepository userRepository;

    public CommonUserDetailsService(SecurityUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SecurityUser> users = userRepository.findAll();
        SecurityUser user = users.stream()
                .filter(securityUser -> securityUser.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(username, user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}