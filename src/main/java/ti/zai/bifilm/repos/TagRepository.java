package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Tag;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	Tag findByName(String name);

	@Query("SELECT t.name FROM Tag t")
	Set<String> findAllNames();
}
