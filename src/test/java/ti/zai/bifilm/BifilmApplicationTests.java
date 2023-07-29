package ti.zai.bifilm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ti.zai.bifilm.models.User;
import ti.zai.bifilm.repos.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class BifilmApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	@Rollback
	void contextLoads() {
		User user = User.builder()
				.nickname("Robert")
				.email("robert@test.com")
				.joinDate(LocalDateTime.now())
				.password("robert123")
				.build();
		userRepository.save(user);

		User userDB = userRepository.findByNickname(user.getNickname());

		System.out.println("User nickname: " + user.getNickname());
		System.out.println("UserDB nickname: " + userDB.getNickname());

		assertEquals(user.getNickname(), userDB.getNickname());
	}

}
