package ti.zai.bifilm.services;

import ti.zai.bifilm.dtos.RegistrationDTO;
import ti.zai.bifilm.models.User;

import java.util.List;

public interface UserService {
	void saveUser(RegistrationDTO registrationDTO);
	User findByEmail(String email);
	User findByNickname(String nickname);
	List<String> findAllUsersNicknames();
	void setAdmin(User user, User grantingAdmin);
	Boolean isAdmin(String nickname);
}
