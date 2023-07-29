package ti.zai.bifilm.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class FullMovieInfoDTO {
	private MovieDTO info;
	private String imagePath;
	private Set<ActorDTO> actors;
	private Set<AuthorDTO> authors;
	private Set<String> tags;
}
