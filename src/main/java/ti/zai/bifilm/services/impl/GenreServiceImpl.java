package ti.zai.bifilm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.repos.GenreRepository;
import ti.zai.bifilm.services.GenreService;

import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {

	private GenreRepository genreRepository;

	@Autowired
	public GenreServiceImpl(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Override
	public Set<String> findAllGenres() {
		return genreRepository.findAllNames();
	}
}
