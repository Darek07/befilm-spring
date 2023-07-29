package ti.zai.bifilm.services;

import ti.zai.bifilm.dtos.ReportDTO;
import ti.zai.bifilm.models.User;

import java.util.List;

public interface ReportService {
	List<ReportDTO> getUserReportHistory(String nickname);
	List<ReportDTO> getAllReports();
	List<ReportDTO> getBlockedUsers();
	void unblockUser(String nickname);
	void saveReportAndDeleteMovie(ReportDTO reportDTO);
}
