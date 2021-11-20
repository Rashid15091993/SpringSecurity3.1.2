package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.User;
import web.service.UserService;

import java.security.Principal;


@Controller
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
		User user = userService.readUser(id);

		if (null == user) {
			attributes.addFlashAttribute("flashMessage", "User are not exists!");
			return "redirect:/users";
		}

		model.addAttribute("user", userService.readUser(id));
		return "form";
	}

	@PostMapping()
	public String saveUser(@ModelAttribute("user") User user, BindingResult bindingResult,
						   RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			return "form";
		}

		userService.createOrUpdateUser(user);
		attributes.addFlashAttribute("flashMessage",
				"User " + user.getName() + " successfully created!");
		return "redirect:/list";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("users") User user, @PathVariable("id") int id) {
		userService.update(id, user);
		return "redirect:/list";
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") long id,
							 RedirectAttributes attributes) {
		User user = userService.deleteUser(id);

		attributes.addFlashAttribute("flashMessage", (null == user) ?
				"User are not exists!" :
				"User " + user.getName() + " successfully deleted!");

		return "redirect:/list";
	}

}