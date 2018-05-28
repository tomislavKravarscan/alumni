package hr.alumni.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.alumni.model.User;
import hr.alumni.model.form.RegistrationForm;
import hr.alumni.service.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public RegistrationController(UserService userService, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping
	public String showRegistrationForm(Model model) {

		model.addAttribute("registrationForm", new RegistrationForm());

		return "register";
	}

	@PostMapping
	public String createUser(Model model, @Valid RegistrationForm registrationForm, BindingResult result,
			HttpSession session, HttpServletRequest request) {

		if (result.hasErrors()) {
			model.addAttribute("registrationForm", registrationForm);
			return "register";
		}

		User user = userService.registerNewUserAccount(registrationForm);

		if (user == null) {
			model.addAttribute("registrationForm", registrationForm);
			result.rejectValue("email", "email.already.exists");
			return "register";
		}

		authenticateUserAndSetSession(registrationForm, request);

		return "redirect:/users/" + user.getUserId();
	}

	private void authenticateUserAndSetSession(RegistrationForm rform, HttpServletRequest request) {
		String username = rform.getEmail();
		String password = rform.getPassword();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		// generate session if one doesn't exist
		request.getSession();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authenticatedUser = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	}

}