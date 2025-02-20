package hu.webuni.flights.security;

import hu.webuni.repository.SecurityUserRepository;
import hu.webuni.security.CommonUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class FlightsUserDetailsService extends CommonUserDetailsService {
    public FlightsUserDetailsService(SecurityUserRepository userRepository) {
        super(userRepository);
    }
}