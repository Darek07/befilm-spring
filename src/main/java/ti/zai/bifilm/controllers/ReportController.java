package ti.zai.bifilm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ti.zai.bifilm.dtos.MovieDTO;
import ti.zai.bifilm.dtos.ReportDTO;
import ti.zai.bifilm.security.SecurityUtil;
import ti.zai.bifilm.services.MovieService;
import ti.zai.bifilm.services.ReportService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Controller
public class ReportController {

	private ReportService reportService;
	private MovieService movieService;

	@Autowired
	public ReportController(ReportService reportService, MovieService movieService) {
		this.reportService = reportService;
		this.movieService = movieService;
	}

	@GetMapping("/reports/{nickname}")
	public String loadUserReportHistory(@PathVariable("nickname") String nickname, Model model) {
		List<ReportDTO> reportHistory = reportService.getUserReportHistory(nickname);
		model.addAttribute("reports", reportHistory);
		model.addAttribute("chosen", "Report History");
		return "reports";
	}

	@GetMapping("/reports")
	public String loadUsersReportHistory(Model model) {
		List<ReportDTO> reportHistory = reportService.getAllReports();
		model.addAttribute("reports", reportHistory);
		model.addAttribute("chosen", "Report History");
		return "reports";
	}

	@GetMapping("/reports/blocked")
	public String loadBlockedUsers(Model model) {
		List<ReportDTO> blockedUsersReports = reportService.getBlockedUsers();
		model.addAttribute("reports", blockedUsersReports);
		model.addAttribute("chosen", "Blocked Users");
		return "reports";
	}

	@PostMapping("/reports/block/{nickname}")
	public String toggleBlockedStatus(@PathVariable("nickname") String nickname,
	                                  @RequestParam("block") String value,
	                                  Model model) {
		if (value.equals("Block")) {
			return "redirect:/create_report/" + nickname + "/Block_User";
		}
		reportService.unblockUser(nickname);
		return "redirect:/reports/blocked?success";
	}

	@GetMapping("/admin/create_report")
	public String adminCreateReport(Model model) {
		List<MovieDTO> movies = movieService.findPublicMovies();
		movies.sort(Comparator.comparing(MovieDTO::getPostDate).reversed());
		model.addAttribute("chosen", "Create Report");
		model.addAttribute("movies", movies);
		model.addAttribute("create_report", true);
		return "layout";
	}

	@GetMapping("/create_report/{nickname}/{movieTitle}")
	public String loadReportForm(@PathVariable("nickname") String nickname,
	                             @PathVariable("movieTitle") String movieTitle,
	                             Model model) {
		movieTitle = URLDecoder.decode(movieTitle, StandardCharsets.UTF_8);
		List<ReportDTO> reportHistory = reportService.getUserReportHistory(nickname);
		ReportDTO reportDTO = ReportDTO.builder()
				.movieTitle(movieTitle)
				.nickname(nickname)
				.build();
		model.addAttribute("report", reportDTO);
		model.addAttribute("reports", reportHistory.size());
		model.addAttribute("chosen", "Create Report");
		return "create_report";
	}

	@PostMapping("/create_report")
	public String createReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
	                           @RequestParam("submit") String submit,
	                           BindingResult result,
	                           Model model) {
		if (result.hasErrors()) {
			model.addAttribute("report", reportDTO);
			model.addAttribute("chosen", "Create Report");
			return "create_report";
		}
		if (submit.equals("Block user")) {
			reportDTO.setBlockUser(true);
		}
		reportService.saveReportAndDeleteMovie(reportDTO);
		return "redirect:/reports?success";
	}

	@GetMapping("/blocked")
	public String loadBlockedUserPage(Model model) {
		String nickname = SecurityUtil.getSessionUser();
		List<ReportDTO> reportHistory = reportService.getUserReportHistory(nickname);
		model.addAttribute("reports", reportHistory);
		model.addAttribute("chosen", "Blocked");
		return "blocked";
	}
}
