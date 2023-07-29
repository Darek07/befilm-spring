package ti.zai.bifilm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.dtos.RegistrationDTO;
import ti.zai.bifilm.models.Admin;
import ti.zai.bifilm.models.User;
import ti.zai.bifilm.repos.AdminRepository;
import ti.zai.bifilm.repos.UserRepository;
import ti.zai.bifilm.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AdminRepository adminRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
	}

	@Override
	public void saveUser(RegistrationDTO registrationDTO) {
		User user = User.builder()
				.joinDate(LocalDateTime.now())
				.nickname(registrationDTO.getNickname())
				.email(registrationDTO.getEmail())
				.password(passwordEncoder.encode(registrationDTO.getPassword()))
				.build();
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	@Override
	public List<String> findAllUsersNicknames() {
		return userRepository.findAllNicknames();
	}

	@Override
	public void setAdmin(User user, User grantingAdmin) {
		Admin admin = Admin.builder()
				.user(user)
				.grantingAdmin(grantingAdmin)
				.build();
		adminRepository.save(admin);
	}

	@Override
	public Boolean isAdmin(String nickname) {
		User user = userRepository.findByNickname(nickname);
		return adminRepository.existsByUser(user);
	}
}
