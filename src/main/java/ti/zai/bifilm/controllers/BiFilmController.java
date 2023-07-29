package ti.zai.bifilm.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ti.zai.bifilm.dtos.*;
import ti.zai.bifilm.security.SecurityUtil;
import ti.zai.bifilm.services.GenreService;
import ti.zai.bifilm.services.MovieService;
import ti.zai.bifilm.services.TagService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class BiFilmController {

	private MovieService movieService;
	private GenreService genreService;
	private TagService tagService;

	@Autowired
	public BiFilmController(MovieService movieService, GenreService genreService, TagService tagService) {
		this.movieService = movieService;
		this.genreService = genreService;
		this.tagService = tagService;
	}

	@GetMapping("/movies/{movieTitle}")
	public String loadUserFullMovieDescription(@PathVariable("movieTitle") String movieTitle, Model model) {
		movieTitle = URLDecoder.decode(movieTitle, StandardCharsets.UTF_8);
		FullMovieInfoDTO fullMovieInfo;
		String nickname = SecurityUtil.getSessionUser();
		Long likes = movieService.getMovieLikes(movieTitle, nickname);
		fullMovieInfo = movieService.findFullMovieInfoByTitleAndUserNickname(movieTitle, nickname);
		model.addAttribute("movie", fullMovieInfo);
		model.addAttribute("likes", likes);
		model.addAttribute("chosen", "Movie");
		model.addAttribute("username", nickname);
		return "movie";
	}

	@GetMapping("/movies/{nickname}/{movieTitle}")
	public String loadUserFullMovieDescription(@PathVariable("nickname") String nickname,
	                                           @PathVariable("movieTitle") String movieTitle,
	                                           Model model) {
		movieTitle = URLDecoder.decode(movieTitle, StandardCharsets.UTF_8);
		String sessionNickname = SecurityUtil.getSessionUser();
		FullMovieInfoDTO fullMovieInfo;
		fullMovieInfo = movieService.findFullMovieInfoByTitleAndUserNickname(movieTitle, nickname);
		Long likes = movieService.getMovieLikes(movieTitle, nickname);
		model.addAttribute("movie", fullMovieInfo);
		model.addAttribute("chosen", "Movie");
		model.addAttribute("username", SecurityUtil.getSessionUser());
		model.addAttribute("admin", true);
		model.addAttribute("likes", likes);
		model.addAttribute("isLike1", movieService.existsLike(1L, sessionNickname, nickname, movieTitle));
		model.addAttribute("isLike2", movieService.existsLike(2L, sessionNickname, nickname, movieTitle));
		return "movie";
	}

	@PostMapping("/movies/{nickname}/{title}/delete")
	public String deleteMovie(@PathVariable("nickname") String nickname,
	                          @PathVariable("title") String movieTitle) {
		if (!Objects.equals(SecurityUtil.getSessionUser(), nickname)) {
			return null;
		}
		movieTitle = URLDecoder.decode(movieTitle, StandardCharsets.UTF_8);
		movieService.deleteMovieByTitleAndNickname(movieTitle, nickname);
		return "redirect:/watched?success";
	}

	@PostMapping("/movies/{nickname}/{title}/like")
	public String likeMovie(@PathVariable("nickname") String nickname,
	                        @PathVariable("title") String movieTitle,
	                        @RequestParam(value = "like1", required = false) String like1,
	                        @RequestParam(value = "like2", required = false) String like2) {
		String sessionNickname = SecurityUtil.getSessionUser();
		movieTitle = URLDecoder.decode(movieTitle, StandardCharsets.UTF_8);
		long value = 0L;
		if (like1 != null) {
			value += 1;
		}
		else if (like2 != null) {
			value += 2;
		}
		movieService.saveLikeOrDelete(movieTitle, nickname, sessionNickname, value);
		return "redirect:/movies/{nickname}/{title}";
	}

	@GetMapping("/new")
	public String loadNewMovieForm(Model model) {
		Set<String> genres = genreService.findAllGenres();
		Set<String> tags = tagService.findAllTags();
		model.addAttribute("chosen", "New Movie");
		model.addAttribute("genres", genres);
		model.addAttribute("tags", tags);

		MovieDTO movieDTO = new MovieDTO();
		model.addAttribute("info", movieDTO);
		return "new";
	}

	@PostMapping("/new")
	public String saveMovie(@Valid @ModelAttribute("info") MovieDTO movieDTO,
	                        @RequestParam("actors") String actorsText,
	                        @RequestParam("authors") String authorsText,
	                        @RequestParam(value = "selectedTags", required = false) String selectedTagsLine,
	                        @RequestParam(value = "uploaded_image", required = false) MultipartFile uploadedImage,
	                        BindingResult result,
	                        Model model) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("info", movieDTO);
			return "new";
		}

		String nickname = SecurityUtil.getSessionUser();
		movieDTO.setNickname(nickname);
		Set<ActorDTO> actors = parseActors(actorsText);
		Set<AuthorDTO> authors = parseAuthors(authorsText);
		List<String> selectedTags;
		if (selectedTagsLine == null) {
			selectedTags = List.of("watched");
		} else {
			selectedTags = Arrays.asList(selectedTagsLine.split(","));
		}

		if (selectedTags.contains("public")) {
			movieDTO.setIsPublic(true);
		}

		String imagePath = null;
		if (uploadedImage != null) {
			imagePath = uploadedImage.getOriginalFilename();
			Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads", imagePath);
			Files.write(fileNameAndPath, uploadedImage.getBytes());
			model.addAttribute("uploaded_image", fileNameAndPath);
		}
		FullMovieInfoDTO fullMovieInfoDTO = FullMovieInfoDTO.builder()
				.info(movieDTO)
				.actors(actors)
				.authors(authors)
				.tags(new HashSet<>(selectedTags))
				.imagePath(imagePath)
				.build();
		movieService.saveMovie(fullMovieInfoDTO);
		return "redirect:/watched?success";
	}

	@GetMapping("/watched")
	public String getWatchedMovies(Model model) {
		return loadMoviesByType(model, "Watched");
	}

	@GetMapping("/towatch")
	public String getToWatchMovies(Model model) {
		return loadMoviesByType(model, "To Watch");
	}

	@GetMapping("/favorite")
	public String getFavoriteMovies(Model model) {
		return loadMoviesByType(model, "Favorite");
	}

	@GetMapping("/public")
	public String getPublicMovies(Model model) {
		return loadMoviesByType(model, "Public");
	}

	@GetMapping("/ratings")
	public String getRatingsMovies(Model model) {
		List<Long> likes = movieService.getAllPublicLikes();
		List<MovieDTO> movies = movieService.findPublicMovies();
		likes.sort(Collections.reverseOrder());
		movies.sort(Comparator.comparing(movie -> movieService.getMovieLikes(movie.getTitle(), movie.getNickname())));
		Collections.reverse(movies);
		model.addAttribute("likes", likes);
		model.addAttribute("movies", movies);
		model.addAttribute("chosen", "Ratings");
		return "layout";
	}

	@GetMapping("/admin")
	public String getAdminPanel(Model model) {
		model.addAttribute("chosen", "Admin Panel");
		return "admin_panel";
	}

	public String loadMoviesByType(Model model, String recordsType) {
		String nickname = SecurityUtil.getSessionUser();
		List<MovieDTO> movies = new ArrayList<>();
		if (nickname != null) {
			movies = switch (recordsType) {
				case "Watched" -> movieService.findWatchedMoviesByUserNickname(nickname);
				case "To Watch" -> movieService.findToWatchMoviesByUserNickname(nickname);
				case "Favorite" -> movieService.findFavoriteMoviesByUserNickname(nickname);
				case "Public" -> movieService.findPublicMovies();
				default -> new ArrayList<>();
			};
		} else if (recordsType.equals("Public")) {
			movies = movieService.findPublicMovies();
		}
		movies.sort(Comparator.comparing(MovieDTO::getPostDate).reversed());
		model.addAttribute("chosen", recordsType);
		model.addAttribute("movies", movies);
		return "layout";
	}

	public Set<ActorDTO> parseActors(String actorsText) {
		Set<ActorDTO> actorDTOs = new HashSet<>();
		String[] actorLines = actorsText.split("\n");

		for (String actorLine : actorLines) {
			String[] actorInfo = actorLine.split(" ");
			if (actorInfo.length != 2) {
				continue;
			}
			String name = actorInfo[0].trim();
			String surname = actorInfo[1].trim();

			ActorDTO actorDTO = ActorDTO.builder().name(name).surname(surname).build();
			actorDTOs.add(actorDTO);
		}
		return actorDTOs;
	}

	public Set<AuthorDTO> parseAuthors(String authorsText) {
		Set<AuthorDTO> authors = new HashSet<>();
		String[] authorLines = authorsText.split("\n");

		for (String authorLine : authorLines) {
			String[] authorInfo = authorLine.split(" ");
			if (authorInfo.length != 3) {
				continue;
			}
			String role = authorInfo[0].trim();
			String name = authorInfo[1].trim();
			String surname = authorInfo[2].trim();

			AuthorDTO authorDTO = AuthorDTO.builder().role(role).name(name).surname(surname).build();
			authors.add(authorDTO);
		}
		return authors;
	}
}
