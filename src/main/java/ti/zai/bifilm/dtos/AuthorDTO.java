package ti.zai.bifilm.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ti.zai.bifilm.models.Author;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {
	private String name;
	private String surname;
	private String role;

	public AuthorDTO(Author author) {
		this.name = author.getName();
		this.surname = author.getSurname();
		this.role = author.getRole();
	}
}
