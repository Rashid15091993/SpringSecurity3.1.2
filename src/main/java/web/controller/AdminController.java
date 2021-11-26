package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String allUsers(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "static/adminPage";
    }

    @GetMapping(value = "add")
    public String addUser(Model model, Principal principal) {
        User user = new User();
        model.addAttribute("userNew", user);
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "addUser";
    }

    @PostMapping(value = "add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam(required = false) String roleAdmin,
                              @RequestParam(required = false) String roleVIP) {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("USER"));

        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(new Role("ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("VIP")) {
            roles.add(new Role("VIP"));
        }
        user.setRoles(roles);
        userService.createUser(user);

        return "redirect:/admin";
    }


    @GetMapping(value = "{id}edit/")
    public String editUser(ModelMap model, @PathVariable("id") Long id, Principal principal) {
        User user = userService.readUser(id);
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.equals(new Role("ADMIN"))) {
                model.addAttribute("roleAdmin", true);
            }
            if (role.equals(new Role("VIP"))) {
                model.addAttribute("roleVIP", true);
            }
        }
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "editUser";
    }

    @PostMapping(value = "edit")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required = false) String roleAdmin,
                               @RequestParam(required = false) String roleVIP) {

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("USER"));
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(new Role("ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("VIP")) {
            roles.add(new Role("VIP"));
        }
        user.setRoles(roles);
        userService.createOrUpdateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
