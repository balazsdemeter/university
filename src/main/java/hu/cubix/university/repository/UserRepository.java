package hu.cubix.university.repository;

import hu.cubix.university.model.UniversityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UniversityUser, String> {
    Optional<UniversityUser> findByFacebookId(String facebookId);
    Optional<UniversityUser> findByGoogleId(String googleId);
}