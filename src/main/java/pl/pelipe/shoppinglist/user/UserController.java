package pl.pelipe.shoppinglist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pelipe.shoppinglist.item.ItemDto;
import pl.pelipe.shoppinglist.item.ItemService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @ModelAttribute
    public void getUser(Model model, Principal principal) {
        if (principal != null)
            model.addAttribute("user", userService.findByUsername(principal.getName()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {

        if (error != null) {
            model.addAttribute("error", "Sorry! Invalid username and/or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "Success! You have been logged out.");
        }
        return "login";
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(Model model, Principal principal) {
        if (principal != null) model.addAttribute("name", userService.findByUsername(principal.getName()).getName());
        return "home";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserEntity());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") UserEntity userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.toString());
            return "/registration";
        }
        userService.save(userForm);
        model.addAttribute("message", "You have been successfully registered.");
        return "/login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        return "profile";
    }

    @RequestMapping(value = "/profile-edit", method = RequestMethod.GET)
    public String profileEdit(Model model) {
        return "profile-edit";
    }

    @RequestMapping(value = "/profile-edit", method = RequestMethod.POST)
    public String profileEdit(@ModelAttribute("userForm") UserEntity userForm,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.toString());
            return "/profile-edit";
        }
        userService.update(userForm);
        model.addAttribute("message", "You have successfully updated your info.");
        return "/profile";
    }
}