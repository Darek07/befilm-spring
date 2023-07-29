package ti.zai.bifilm.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {
	@NotEmpty
	@Email(message = "Invalid email format.")
	private String email;

	@NotEmpty
	private String nickname;

	@NotEmpty
	@Size(min = 8, message = "Password must be at least 8 characters long.")
	private String password;
}
