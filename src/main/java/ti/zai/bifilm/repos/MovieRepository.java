package ti.zai.bifilm.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ti.zai.bifilm.dtos.ActorDTO;
import ti.zai.bifilm.dtos.AuthorDTO;
import ti.zai.bifilm.dtos.MovieDTO;
import ti.zai.bifilm.models.Movie;
import ti.zai.bifilm.models.Tag;
import ti.zai.bifilm.models.User;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("SELECT m.imagePath FROM Movie m WHERE m.title = ?1 AND m.user = ?2")
	String findImagePathByTitleAndUser(String title, User user);

	@Query("SELECT new ti.zai.bifilm.dtos.MovieDTO(m) FROM Movie m JOIN m.tags t " +
			"WHERE m.user.id = ?1 AND t.name = ?2 ORDER BY m.postDate DESC")
	List<MovieDTO> findByUserIdAndTagOrderByPostDateDesc(Long userID, String tagName, Pageable pageable);

	@Query("SELECT new ti.zai.bifilm.dtos.MovieDTO(m) FROM Movie m WHERE m.isPublic = true")
	List<MovieDTO> findAllByIsPublicTrue(Pageable pageable);

	@Query("SELECT new ti.zai.bifilm.dtos.MovieDTO(m) FROM Movie m WHERE m.title = ?1 AND m.user = ?2")
	MovieDTO findByTitleAndUser(String title, User user);

	@Query("SELECT new ti.zai.bifilm.dtos.ActorDTO(a) FROM Movie m JOIN m.actors a WHERE m.title = ?1 AND m.user = ?2")
	Set<ActorDTO> findActorsByTitleAndUser(String title, User user);

	@Query("SELECT new ti.zai.bifilm.dtos.AuthorDTO(a) FROM Movie m JOIN m.authors a WHERE m.title = ?1 AND m.user = ?2")
	Set<AuthorDTO> findAuthorsByTitleAndUser(String title, User user);

	@Query("SELECT t.name FROM Movie m JOIN m.tags t WHERE m.title = ?1 AND m.user = ?2")
	Set<String> findTagsNameByTitleAndUser(String title, User user);

	@Query("SELECT m FROM Movie m WHERE m.title = ?1 AND m.user = ?2")
	Movie findMovieByTitleAndUser(String title, User user);

	@Modifying
	@Query("DELETE FROM Movie m WHERE m.id = ?1")
	void deleteById(Long movieId);
}
