package web.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	@GetMapping({"", "/", "list"})
	public String showAllUsers(Model model, @ModelAttribute("flashMessage") String flashAttribute) {
		model.addAttribute("users", userService.getAllUsers());

		return "list";
	}

	@RequestMapping(value = "/user_info", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserName(Principal principal) {
		return principal.getName();
	}

	@GetMapping(value = "/new")
	public String addUserForm(@ModelAttribute("user") User user) {
		return "form";
	}

	@GetMapping("/{id}/edit")
	public String editUserForm(@PathVariable(value = "id", required = true) long id, Model model,
								RedirectAttributes attributes) {
		try {
			model.addAttribute("user", userService.readUser(id));
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();

			return "redirect:/users";
		}

		return "form";
	}

	@PostMapping()
	public String saveUser(@ModelAttribute("user") User user, BindingResult bindingResult,
						   RedirectAttributes attributes, @RequestParam(value = "rolesSet", required = false) String[] rolesSet) {
		if (bindingResult.hasErrors()) {
			return "form";
		}
		Set<Role> roles = new HashSet<>();
		for (String s : rolesSet){
			roles.add(new Role(s));
		}
		user.setRoles(roles);

		userService.createOrUpdateUser(user);
		attributes.addFlashAttribute("flashMessage",
				"User " + user.getName() + " successfully created!");
		return "redirect:/users";
	}


	@GetMapping("/{id}/delete")
	public String deleteUser(@PathVariable("id") int userId) {
		userService.deleteUser(userId);

		return "redirect:/users";
	}

}