package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userID", referencedColumnName = "id", unique = true, nullable = false)
	private User user;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "grantingAdminID", referencedColumnName = "id")
	private User grantingAdmin;
}
