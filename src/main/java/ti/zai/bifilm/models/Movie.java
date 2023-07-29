package ti.zai.bifilm.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private LocalDateTime postDate = LocalDateTime.now();

	private String imagePath;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Year premiere;

	@Column(nullable = false)
	private String country;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private Boolean isPublic = false;

	@ManyToOne
	@JoinColumn(name = "genreID", referencedColumnName = "id", nullable = false)
	private Genre genre;

	@ManyToOne
	@JoinColumn(name = "userID", referencedColumnName = "id", nullable = false)
	private User user;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "movie_actor",
			joinColumns = @JoinColumn(name = "movieID"),
			inverseJoinColumns = @JoinColumn(name = "actorID")
	)
	private Set<Actor> actors = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "movie_author",
			joinColumns = @JoinColumn(name = "movieID"),
			inverseJoinColumns = @JoinColumn(name = "authorID")
	)
	private Set<Author> authors = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "movie_tag",
			joinColumns = @JoinColumn(name = "movieID", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "tagID", referencedColumnName = "id")
	)
	private Set<Tag> tags = new HashSet<>();
}
