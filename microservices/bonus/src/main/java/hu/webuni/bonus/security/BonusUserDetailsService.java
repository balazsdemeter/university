package hu.webuni.bonus.security;

import hu.webuni.repository.SecurityUserRepository;
import hu.webuni.security.CommonUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class BonusUserDetailsService extends CommonUserDetailsService {
    public BonusUserDetailsService(SecurityUserRepository userRepository) {
        super(userRepository);
    }
}
