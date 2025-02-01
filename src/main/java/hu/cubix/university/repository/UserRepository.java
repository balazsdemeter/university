package hu.cubix.university.repository;

import hu.cubix.university.model.UniversityUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UniversityUser, String> {
}