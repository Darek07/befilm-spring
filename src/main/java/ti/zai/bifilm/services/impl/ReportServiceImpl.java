package ti.zai.bifilm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.dtos.ReportDTO;
import ti.zai.bifilm.models.Movie;
import ti.zai.bifilm.models.Report;
import ti.zai.bifilm.models.User;
import ti.zai.bifilm.repos.MovieRepository;
import ti.zai.bifilm.repos.ReportRepository;
import ti.zai.bifilm.repos.UserRepository;
import ti.zai.bifilm.services.ReportService;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

	private ReportRepository reportRepository;
	private UserRepository userRepository;
	private MovieRepository movieRepository;

	@Autowired
	public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository, MovieRepository movieRepository) {
		this.reportRepository = reportRepository;
		this.userRepository = userRepository;
		this.movieRepository = movieRepository;
	}

	@Override
	public List<ReportDTO> getUserReportHistory(String nickname) {
		User user = userRepository.findByNickname(nickname);
		return reportRepository.findByUser(user);
	}

	@Override
	public List<ReportDTO> getAllReports() {
		return reportRepository.findAllReports();
	}

	@Override
	public List<ReportDTO> getBlockedUsers() {
		return reportRepository.findAllByBlockUserIsTrue();
	}

	@Override
	public void unblockUser(String nickname) {
		User user = userRepository.findByNickname(nickname);
		var reports = reportRepository.findAllByUserOrderByDateTimeDesc(user);
		reports.forEach(report -> report.setBlockUser(false));
		reportRepository.saveAll(reports);
	}

	@Override
	public void saveReportAndDeleteMovie(ReportDTO reportDTO) {
		User user = userRepository.findByNickname(reportDTO.getNickname());
		Report report = Report.builder()
				.description(reportDTO.getDescription())
				.blockUser(reportDTO.getBlockUser())
				.dateTime(reportDTO.getDateTime())
				.movieTitle(reportDTO.getMovieTitle())
				.user(user)
				.build();
		reportRepository.save(report);

		Movie movie = movieRepository.findMovieByTitleAndUser(reportDTO.getMovieTitle(), user);
		if (movie == null) {
			return;
		}
		movie.setIsPublic(false);
		movieRepository.save(movie);
	}
}
