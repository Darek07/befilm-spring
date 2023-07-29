package ti.zai.bifilm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ti.zai.bifilm.dtos.RegistrationDTO;
import ti.zai.bifilm.models.User;
import ti.zai.bifilm.security.SecurityUtil;
import ti.zai.bifilm.services.UserService;

import java.util.List;

@Controller
public class AuthController {

	private UserService userService;

	@Autowired
	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/register")
	public String getRegisterForm(Model model) {
		RegistrationDTO user = new RegistrationDTO();
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") RegistrationDTO user,
	                       BindingResult result, Model model) {
		User existingUserEmail = userService.findByEmail(user.getEmail());
		if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
			return "redirect:/register?fail";
		}
		User existingUserNickname = userService.findByNickname(user.getNickname());
		if(existingUserNickname != null && existingUserNickname.getNickname() != null && !existingUserNickname.getNickname().isEmpty()) {
			return "redirect:/register?fail";
		}
		if(result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}
		userService.saveUser(user);
		return "redirect:/login?success";
	}

	@GetMapping("/admin/add")
	public String loadAddAdmin(Model model) {
		List<String> nicknames = userService.findAllUsersNicknames();
		String sessionNickname = SecurityUtil.getSessionUser();
		nicknames.remove(sessionNickname);
		nicknames.removeIf(nickname -> userService.isAdmin(nickname));
		model.addAttribute("chosen", "Add admin");
		model.addAttribute("users", nicknames);
		return "users_search";
	}

	@GetMapping("/admin/add/{nickname}")
	public String addAdmin(@PathVariable("nickname") String nickname) {
		User user = userService.findByNickname(nickname);
		String grantingNickname = SecurityUtil.getSessionUser();
		User grantingUser = userService.findByNickname(grantingNickname);
		userService.setAdmin(user, grantingUser);
		return "redirect:/admin?success";
	}
}
