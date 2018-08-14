package pl.pelipe.shoppinglist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

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
    public String registration(@ModelAttribute("userForm") UserEntity userForm, BindingResult bindingResult, Model model) throws IOException {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.toString());
            return "/registration";
        }
        userService.save(userForm);
        model.addAttribute("message", "You have been successfully registered.");
        return "login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        return "profile";
    }

    @RequestMapping(value = "/profile-edit", method = RequestMethod.GET)
    public String profileEdit() {
        return "profile-edit";
    }

    @RequestMapping(value = "/profile-edit", method = RequestMethod.POST)
    public String profileEdit(@ModelAttribute("userForm") UserEntity userForm,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.toString());
            return "profile-edit";
        }
        userService.update(userForm);
        model.addAttribute("message", "You have successfully updated your info.");
        return "profile";
    }

    @RequestMapping(value = "/password-recovery", method = RequestMethod.GET)
    public String passwordRecovery(Model model) {
        model.addAttribute("user", new UserEntity());
        return "password-recovery";
    }

    @RequestMapping(value = "/password-recovery", method = RequestMethod.POST)
    public String sendPasswordRecoveryToken(@ModelAttribute("user") UserEntity user, Model model) throws IOException {
        Boolean result = userService.sendPasswordResetToken(user.getUsername());
        if (result) {
            model.addAttribute("message", "We have sent you an e-mail with further instructions.");
            return "login";
        } else {
            model.addAttribute("error", "We could not find your e-mail");
            return "password-recovery";
        }
    }

    @RequestMapping(value = "/password-reset", method = RequestMethod.GET)
    public String passwordResetPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        return "password-reset";
    }

    @RequestMapping(value = "/password-reset", method = RequestMethod.POST)
    public String passwordResetRequest(
            @ModelAttribute("passwordResetRequest") PasswordResetRequest passwordResetRequest,
            Model model, RedirectAttributes redirectAttributes) throws IOException {
        String result = userService.resetPassword(
                passwordResetRequest.getTokenValue(),
                passwordResetRequest.getNewPassword(),
                passwordResetRequest.getNewPasswordConfirm());
        if (result.equals("Success")) {
            model.addAttribute("message", "Your password has been changed");
            return "login";
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/password-reset" + "?token=" + passwordResetRequest.tokenValue;
        }
    }

    private static class PasswordResetRequest {
        private String tokenValue;
        private String newPassword;
        private String newPasswordConfirm;

        String getTokenValue() {
            return tokenValue;
        }

        String getNewPassword() {
            return newPassword;
        }

        String getNewPasswordConfirm() {
            return newPasswordConfirm;
        }

        public void setTokenValue(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public void setNewPasswordConfirm(String newPasswordConfirm) {
            this.newPasswordConfirm = newPasswordConfirm;
        }
    }
}