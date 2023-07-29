package ti.zai.bifilm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ti.zai.bifilm.models.User;
import ti.zai.bifilm.repos.AdminRepository;
import ti.zai.bifilm.repos.ReportRepository;
import ti.zai.bifilm.repos.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	private AdminRepository adminRepository;
	private ReportRepository reportRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository, ReportRepository reportRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.reportRepository = reportRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByNickname(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		List<String> roles = new ArrayList<>();
		if (adminRepository.existsByUser(user)) {
			roles.add("ADMIN");
		}
		if (reportRepository.existsByUserAndBlockUserTrue(user)) {
			roles.add("BLOCKED");
		} else {
			roles.add("USER");
		}
		org.springframework.security.core.userdetails.User authUser =
				new org.springframework.security.core.userdetails.User(
						user.getNickname(),
						user.getPassword(),
						roles.stream().map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList())
				);
		return authUser;
	}
}
