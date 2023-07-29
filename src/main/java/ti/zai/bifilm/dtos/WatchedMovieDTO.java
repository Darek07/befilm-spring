package ti.zai.bifilm.dtos;

import lombok.Data;

import java.time.Year;

@Data
public class WatchedMovieDTO {
	private String title;
	private Year premiere;
	private String country;
	private String description;
	private Boolean isPublic;
}
