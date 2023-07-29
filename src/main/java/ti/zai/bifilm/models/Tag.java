package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "Tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
	private Set<Movie> movies;
}
