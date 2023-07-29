package ti.zai.bifilm.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ti.zai.bifilm.dtos.ReportDTO;
import ti.zai.bifilm.models.Report;
import ti.zai.bifilm.models.User;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

	@Query("SELECT new ti.zai.bifilm.dtos.ReportDTO(r) FROM Report r WHERE r.user = ?1 ORDER BY r.dateTime DESC")
	List<ReportDTO> findByUser(User user);

	@Query("SELECT new ti.zai.bifilm.dtos.ReportDTO(r) FROM Report r ORDER BY r.dateTime DESC")
	List<ReportDTO> findAllReports();

	@Query("SELECT new ti.zai.bifilm.dtos.ReportDTO(r) FROM Report r WHERE r.blockUser = true ORDER BY r.dateTime DESC")
	List<ReportDTO> findAllByBlockUserIsTrue();

	List<Report> findAllByUserOrderByDateTimeDesc(User user);

	Boolean existsByUserAndBlockUserTrue(User user);
}
