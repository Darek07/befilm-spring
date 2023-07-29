package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.models.Admin;
import ti.zai.bifilm.models.User;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByUser(User user);
}
