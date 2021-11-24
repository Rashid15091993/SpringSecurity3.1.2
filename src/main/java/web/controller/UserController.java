package web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "static/userPage";
    }
//    @GetMapping(value = "/")
//    public String getUserPage(ModelMap modelMap) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        modelMap.addAttribute("user", user);
//        return "userPage";
//    }


//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") Long id, ModelMap modelMap) {
//        modelMap.addAttribute("user", userService.getUserById(id));
//        return "userPage";
//    }
}
