package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Genre;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query("SELECT g.name FROM Genre g")
	Set<String> findAllNames();

	Genre findByName(String genreName);
}
