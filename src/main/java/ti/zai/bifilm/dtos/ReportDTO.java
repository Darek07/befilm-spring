package ti.zai.bifilm.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ti.zai.bifilm.models.Report;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
	@NotEmpty
	@NotNull
	private String description;
	private Boolean blockUser = false;
	private LocalDateTime dateTime = LocalDateTime.now();
	@NotEmpty
	@NotNull
	private String movieTitle;
	@NotEmpty
	@NotNull
	private String nickname;

	public ReportDTO(Report report) {
		this.description = report.getDescription();
		this.blockUser = report.getBlockUser();
		this.dateTime = report.getDateTime();
		this.movieTitle = report.getMovieTitle();
		this.nickname = report.getUser().getNickname();
	}
}
