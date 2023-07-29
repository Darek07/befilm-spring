package ti.zai.bifilm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.dtos.ActorDTO;
import ti.zai.bifilm.dtos.AuthorDTO;
import ti.zai.bifilm.dtos.FullMovieInfoDTO;
import ti.zai.bifilm.dtos.MovieDTO;
import ti.zai.bifilm.models.*;
import ti.zai.bifilm.repos.*;
import ti.zai.bifilm.security.SecurityUtil;
import ti.zai.bifilm.services.MovieService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

	private MovieRepository movieRepository;
	private UserRepository userRepository;
	private TagRepository tagRepository;
	private GenreRepository genreRepository;
	private ActorRepository actorRepository;
	private AuthorRepository authorRepository;
	private LikeRepository likeRepository;

	@Autowired
	public MovieServiceImpl(MovieRepository movieRepository, UserRepository userRepository, TagRepository tagRepository, GenreRepository genreRepository, ActorRepository actorRepository, AuthorRepository authorRepository, LikeRepository likeRepository) {
		this.movieRepository = movieRepository;
		this.userRepository = userRepository;
		this.tagRepository = tagRepository;
		this.genreRepository = genreRepository;
		this.actorRepository = actorRepository;
		this.authorRepository = authorRepository;
		this.likeRepository = likeRepository;
	}

	@Override
	public List<MovieDTO> findMoviesByUserNicknameAndTag(String nickname, String tagName) {
		User user = userRepository.findByNickname(nickname);
		if (user == null) {
			return new ArrayList<>();
		}
		PageRequest pageRequest = PageRequest.of(0, 15);
		return movieRepository.findByUserIdAndTagOrderByPostDateDesc(user.getId(), tagName, pageRequest);
	}

	@Override
	public List<MovieDTO> findWatchedMoviesByUserNickname(String nickname) {
		return findMoviesByUserNicknameAndTag(nickname, "watched");
	}

	@Override
	public List<MovieDTO> findToWatchMoviesByUserNickname(String nickname) {
		return findMoviesByUserNicknameAndTag(nickname, "towatch");
	}

	@Override
	public List<MovieDTO> findFavoriteMoviesByUserNickname(String nickname) {
		return findMoviesByUserNicknameAndTag(nickname, "favorite");
	}

	@Override
	public List<MovieDTO> findPublicMovies() {
		PageRequest pageRequest = PageRequest.of(0, 15);
		return movieRepository.findAllByIsPublicTrue(pageRequest);
	}

	@Override
	public FullMovieInfoDTO findFullMovieInfoByTitleAndUserNickname(String title, String nickname) {
		User user = userRepository.findByNickname(nickname);
		MovieDTO movie = movieRepository.findByTitleAndUser(title, user);
		Set<ActorDTO> actors = movieRepository.findActorsByTitleAndUser(title, user);
		Set<AuthorDTO> authors = movieRepository.findAuthorsByTitleAndUser(title, user);
		Set<String> tags = movieRepository.findTagsNameByTitleAndUser(title, user);
		String imagePath = movieRepository.findImagePathByTitleAndUser(title, user);

		return FullMovieInfoDTO.builder()
				.info(movie)
				.imagePath(imagePath)
				.actors(actors)
				.authors(authors)
				.tags(tags)
				.build();
	}

	@Override
	public Movie saveMovie(FullMovieInfoDTO fullMovieInfoDTO) {
		String nickname = SecurityUtil.getSessionUser();
		MovieDTO movieDTO = fullMovieInfoDTO.getInfo();
		Movie movie = Movie.builder()
				.postDate(LocalDateTime.now())
				.imagePath(fullMovieInfoDTO.getImagePath())
				.title(movieDTO.getTitle())
				.premiere(movieDTO.getPremiere())
				.country(movieDTO.getCountry())
				.description(movieDTO.getDescription())
				.isPublic(movieDTO.getIsPublic())
				.genre(genreRepository.findByName(movieDTO.getGenre()))
				.user(userRepository.findByNickname(nickname))
				.actors(mapToActors(fullMovieInfoDTO.getActors()))
				.authors(mapToAuthors(fullMovieInfoDTO.getAuthors()))
				.tags(mapToTags(fullMovieInfoDTO.getTags()))
				.build();
		return movieRepository.save(movie);
	}

	@Override
	public void deleteMovieByTitleAndNickname(String title, String nickname) {
		User user = userRepository.findByNickname(nickname);
		Movie movie = movieRepository.findMovieByTitleAndUser(title, user);
		if (movie == null) {
			return;
		}
		movieRepository.deleteById(movie.getId());
	}

	@Override
	public Long getMovieLikes(String title, String nickname) {
		User user = userRepository.findByNickname(nickname);
		Movie movie = movieRepository.findMovieByTitleAndUser(title, user);
		Long likes = likeRepository.countByMovie(movie);
		return likes == null ? 0 : likes;
	}

	@Override
	public void saveLikeOrDelete(String title, String movieNickname, String sessionNickname, Long value) {
		User sessionUser = userRepository.findByNickname(sessionNickname);
		User movieUser = userRepository.findByNickname(movieNickname);
		Movie movie = movieRepository.findMovieByTitleAndUser(title, movieUser);
		Like like = likeRepository.findByValueAndUserAndMovie(value, sessionUser, movie);
		if (like != null) {
			likeRepository.deleteById(like.getId());
			return;
		}
		like = Like.builder().value(value).user(sessionUser).movie(movie).build();
		likeRepository.save(like);
	}

	@Override
	public Boolean existsLike(Long value, String sessionNickname, String movieNickname, String title) {
		User sessionUser = userRepository.findByNickname(sessionNickname);
		User movieUser = userRepository.findByNickname(movieNickname);
		Movie movie = movieRepository.findMovieByTitleAndUser(title, movieUser);
		return likeRepository.findByValueAndUserAndMovie(value, sessionUser, movie) != null;
	}

	@Override
	public List<Long> getAllPublicLikes() {
		List<MovieDTO> movies = findPublicMovies();
		List<Long> likes = new ArrayList<>();
		for (MovieDTO movieDTO : movies) {
			Long likesCount = getMovieLikes(movieDTO.getTitle(), movieDTO.getNickname());
			likes.add(likesCount == null ? 0 : likesCount);
		}
		return likes;
	}

	public Set<Actor> mapToActors(Set<ActorDTO> actors) {
		Set<Actor> result = new HashSet<>();
		for (ActorDTO actorDTO : actors) {
			var existingActor = actorRepository.findByNameAndSurname(actorDTO.getName(), actorDTO.getSurname());
			if (existingActor != null) {
				result.add(existingActor);
			} else {
				result.add(Actor.builder()
						.name(actorDTO.getName())
						.surname(actorDTO.getSurname())
						.build());
			}
		}
		return result;
	}

	public Set<Author> mapToAuthors(Set<AuthorDTO> authors) {
		Set<Author> result = new HashSet<>();
		for (AuthorDTO authorDTO : authors) {
			Author existingAuthor = authorRepository.findByRoleAndNameAndSurname(
					authorDTO.getRole(), authorDTO.getName(), authorDTO.getSurname());
			if (existingAuthor != null) {
				result.add(existingAuthor);
			} else {
				result.add(Author.builder()
						.name(authorDTO.getName())
						.surname(authorDTO.getSurname())
						.role(authorDTO.getRole())
						.build());
			}
		}
		return result;
	}

	public Set<Tag> mapToTags(Set<String> tags) {
		Set<Tag> result = new HashSet<>();
		for (String tagName : tags) {
			Tag existingTag = tagRepository.findByName(tagName);
			if (existingTag != null) {
				result.add(existingTag);
			} else {
				result.add(Tag.builder()
						.name(tagName)
						.build());
			}
		}
		return result;
	}
}
