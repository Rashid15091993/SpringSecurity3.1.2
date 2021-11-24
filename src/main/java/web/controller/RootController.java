package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RootController {
    private final UserService userService;

    @Autowired
    public RootController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/")
    public String getHomePage() {
        return "redirect:login";

    }

    @RequestMapping(value = "hello")
    public String getHelloPage(Model model, Principal principal) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC-SECURITY application");
        messages.add("This is Root Page");

        model.addAttribute("messages", messages);
        if (principal != null) {
            model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        }

        return "static/helloPage";
    }

//    @GetMapping(value = "login")
//    public String getLoginPage() {
//        return "loginPage";
//    }

    @GetMapping(value = "vip")
    public String getVipPage(Model model, Principal principal) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC-SECURITY application");
        messages.add("This is VIP Page");
        model.addAttribute("messages", messages);
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "static/vipPage";
    }
}
