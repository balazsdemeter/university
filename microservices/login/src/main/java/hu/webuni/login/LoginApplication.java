package hu.webuni.login;

import hu.webuni.model.SecurityUser;
import hu.webuni.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication
@ComponentScan(basePackages = {"hu.webuni", "hu.webuni.security"})
@EnableJpaRepositories(basePackages = {"hu.webuni.repository"})
@EntityScan("hu.webuni.model")
public class LoginApplication implements CommandLineRunner {
    private final SecurityUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new SecurityUser("user", passwordEncoder.encode("pass"), Set.of("user", "bonus")));
        userRepository.save(new SecurityUser("searchUser", passwordEncoder.encode("pass"), Set.of("search")));
    }
}