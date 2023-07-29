package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime joinDate = LocalDateTime.now();

	@Column(nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String password;
}
