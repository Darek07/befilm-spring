package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Like;
import ti.zai.bifilm.models.Movie;
import ti.zai.bifilm.models.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	@Query("SELECT SUM(l.value) FROM Like l WHERE l.movie = ?1")
	Long countByMovie(Movie movie);
	Like findByValueAndUserAndMovie(Long value, User user, Movie movie);
}
