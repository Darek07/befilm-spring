package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
	Actor findByNameAndSurname(String name, String surname);
}
