package hr.alumni.web;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hr.alumni.model.Role;
import hr.alumni.model.User;
import hr.alumni.model.details.UserDetailsBasic;
import hr.alumni.model.details.UserDetailsMore;
import hr.alumni.model.form.EditUserForm;
import hr.alumni.repository.RoleRepository;
import hr.alumni.repository.UserRepository;
import hr.alumni.service.FormFactory;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final FormFactory formFactory;

	@Autowired
	public UserController(UserRepository userRepository, FormFactory formFactory, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.formFactory = formFactory;
		this.roleRepository = roleRepository;
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public String showUserList(Model model) {
		List<User> users = userRepository.findAll();
		List<UserDetailsBasic> basicDetailsUsers = new ArrayList<>();

		users.forEach(user -> basicDetailsUsers.add(new UserDetailsBasic(user)));

		model.addAttribute("users", basicDetailsUsers);
		return "users";
	}

	@RequestMapping(value = "/{id}/changeRole", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public String changeRole(Model model, @PathVariable UUID id, @RequestParam(required = true) String newRole) {

		User user = userRepository.getOne(id);

		
		Role role = roleRepository.findByName(newRole);
		
		if(role == null) {
			return "profile";
		}
		
		user.getRoles().clear();
		user.getRoles().add(role);
		userRepository.save(user);

		model.addAttribute("user", new UserDetailsMore(user));

		return "profile";
	}

	@GetMapping("/{id}")
	@PreAuthorize("@webSecurityConfig.checkUserId(authentication, #id)")
	public String showUserProfile(Model model, @PathVariable UUID id) {
		User user = userRepository.findOne(id);

		if (user == null) {
			model.addAttribute("errorMessage", "Taj korisnik ne postoji!");
			return "customError";
		}

		UserDetailsMore userDetails = new UserDetailsMore(user);
		model.addAttribute("user", userDetails);

		return "profile";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	@PreAuthorize("@webSecurityConfig.checkUserId(authentication, #id)")
	public String editUser(Model model, @PathVariable UUID id, HttpSession session) {
		User user = userRepository.getOne(id);

		if (user == null) {
			model.addAttribute("errorMessage", "Taj korisnik ne postoji!");
			return "customError";
		}

		model.addAttribute("user", new UserDetailsMore(user));

		return "editUser";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	@PreAuthorize("@webSecurityConfig.checkUserId(authentication, #id)")
	public String updateUser(Model model, @PathVariable UUID id, HttpSession session, @Valid EditUserForm editUserForm,
			BindingResult result) {

		User user = userRepository.getOne(id);
		if (result.hasErrors()) {
			model.addAttribute("user", new UserDetailsMore(user));
			model.addAttribute("editUserForm", editUserForm);

			return "editUser";
		}

		formFactory.editUserFromForm(user, editUserForm);
		userRepository.save(user);
		
		return "redirect:/users/" + id.toString();
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@PreAuthorize("@webSecurityConfig.checkUserId(authentication, #id)")
	public String deleteUser(Model model, @PathVariable UUID id, HttpSession session) {
		userRepository.delete(userRepository.findOne(id));

		return "users";
	}
}
