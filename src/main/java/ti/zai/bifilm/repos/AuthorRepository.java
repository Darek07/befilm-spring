package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	Author findByRoleAndNameAndSurname(String role, String name, String surname);
}
