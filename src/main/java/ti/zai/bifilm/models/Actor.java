package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Actors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String surname;

	@ManyToMany(mappedBy = "actors", cascade = CascadeType.ALL)
	private Set<Movie> movies = new HashSet<>();
}
