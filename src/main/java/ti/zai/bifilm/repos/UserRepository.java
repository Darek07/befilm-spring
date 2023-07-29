package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByNickname(String nickname);

	@Query("SELECT u.nickname FROM User u")
	List<String> findAllNicknames();
}
