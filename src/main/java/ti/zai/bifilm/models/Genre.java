package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
}
