package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String description;

	@Column(nullable = false)
	private Boolean blockUser = false;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private LocalDateTime dateTime = LocalDateTime.now();

	// todo should it be Movie?
	private String movieTitle;

	@ManyToOne
	@JoinColumn(name = "userID", referencedColumnName = "id", nullable = false)
	private User user;
}
