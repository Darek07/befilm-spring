package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// todo change to enum?
	@Column(nullable = false)
	private Long value;

	@ManyToOne
	@JoinColumn(name = "userID", referencedColumnName = "id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "movieID", referencedColumnName = "id", nullable = false)
	private Movie movie;
}
