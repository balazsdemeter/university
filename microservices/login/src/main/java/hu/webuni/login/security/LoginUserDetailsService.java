package hu.webuni.login.security;

import hu.webuni.repository.SecurityUserRepository;
import hu.webuni.security.CommonUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsService extends CommonUserDetailsService {
    public LoginUserDetailsService(SecurityUserRepository userRepository) {
        super(userRepository);
    }
}