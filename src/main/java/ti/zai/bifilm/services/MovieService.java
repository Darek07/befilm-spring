package ti.zai.bifilm.services;

import ti.zai.bifilm.dtos.FullMovieInfoDTO;
import ti.zai.bifilm.dtos.MovieDTO;
import ti.zai.bifilm.models.Movie;

import java.util.List;

public interface MovieService {
	List<MovieDTO> findMoviesByUserNicknameAndTag(String nickname, String tagName);
	List<MovieDTO> findWatchedMoviesByUserNickname(String nickname);
	List<MovieDTO> findToWatchMoviesByUserNickname(String nickname);
	List<MovieDTO> findFavoriteMoviesByUserNickname(String nickname);
	List<MovieDTO> findPublicMovies();
	FullMovieInfoDTO findFullMovieInfoByTitleAndUserNickname(String title, String nickname);
	Movie saveMovie(FullMovieInfoDTO fullMovieInfoDTO);
	void deleteMovieByTitleAndNickname(String title, String nickname);
	Long getMovieLikes(String title, String nickname);
	void saveLikeOrDelete(String title, String movieNickname, String nickname, Long value);
	Boolean existsLike(Long value, String sessionNickname, String movieNickname, String title);
	List<Long> getAllPublicLikes();
}
