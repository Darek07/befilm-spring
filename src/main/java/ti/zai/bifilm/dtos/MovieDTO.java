package ti.zai.bifilm.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ti.zai.bifilm.models.Movie;

import java.time.LocalDateTime;
import java.time.Year;

@Data
@NoArgsConstructor
public class MovieDTO {
	private LocalDateTime postDate;
	private String title;
	private Year premiere;
	@NotEmpty
	private String country;
	@NotEmpty
	private String description;
	private Boolean isPublic = false;
	@NotEmpty
	private String genre;
	private String nickname;

	public MovieDTO(Movie movie) {
		this.postDate = movie.getPostDate();
		this.title = movie.getTitle();
		this.premiere = movie.getPremiere();
		this.country = movie.getCountry();
		this.description = movie.getDescription();
		this.isPublic = movie.getIsPublic();
		this.genre = movie.getGenre().getName();
		this.nickname = movie.getUser().getNickname();
	}
}
