package ti.zai.bifilm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.repos.TagRepository;
import ti.zai.bifilm.services.TagService;

import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

	private TagRepository tagRepository;

	@Autowired
	public TagServiceImpl(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	public Set<String> findAllTags() {
		return tagRepository.findAllNames();
	}
}
