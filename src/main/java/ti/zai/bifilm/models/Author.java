package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String surname;

	@Column(nullable = false)
	private String role;

	@ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
	private Set<Movie> movies = new HashSet<>();
}
