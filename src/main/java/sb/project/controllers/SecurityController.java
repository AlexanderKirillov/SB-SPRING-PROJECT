package sb.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sb.project.domain.User;
import sb.project.repositories.UserRepository;
import sb.project.services.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "security/login";
    }

    @GetMapping(value = "/access-error")
    public String accessErrorPage(Model model) {
        return "error/access-denied-page";
    }

    @GetMapping(value = {"/registration"})
    public String registrationPage(Model model) {
        User user = new User();

        model.addAttribute("user", user);

        return "security/registration";
    }

    @GetMapping(value = {"/resetPassword"})
    public String resetPasswordPage(Model model) {
        User user = new User();

        model.addAttribute("user", user);

        return "security/reset-password";
    }

    @PostMapping(value = {"/registration"})
    public String registration(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("gendername") String gendername, Locale locale, HttpServletRequest request) throws Exception {
        List<User> userList = userRepository.findAll();

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.password", "Пароли не совпадают!");
        }

        for (User exuser : userList) {
            if (user.getUserName().equals(exuser.getUserName())) {
                bindingResult.rejectValue("userName", "error.userName", "Пользователь с таким никнейном уже зарегистрирован!");
            }
        }

        for (User exuser : userList) {
            if (user.getEmail().toLowerCase().equals(exuser.getEmail().toLowerCase())) {
                bindingResult.rejectValue("email", "error.email", "Пользователь с такой электронной почтой уже зарегистрирован!");
            }
        }

        if (bindingResult.hasErrors()) {
            return "security/registration";

        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setActive(false);
            user.setEmail(user.getEmail().toLowerCase());
            user.setRoles("ROLE_USER");
            user.setGender(gendername);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActivationToken(emailService.generateToken(user.getUserName()));
            emailService.sendConfirmationMail(user.getEmail(), user.getActivationToken(), locale, request);
            userRepository.save(user);

            return "other/successful-registration";
        }
    }

    @PostMapping(value = {"/resetPassword"})
    public String resetPassword(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, Locale locale, HttpServletRequest request) throws Exception {
        List<User> userList = userRepository.findAll();

        Optional<User> originalUser = userRepository.findByEmail(user.getEmail());
        if (!originalUser.isPresent()) {
            bindingResult.rejectValue("email", "error.email", "Пользователь с указанным e-mail не зарегистрирован!");
        }

        if (bindingResult.hasErrors()) {
            return "security/reset-password";
        } else {
            originalUser.get().setResetPassToken(emailService.generateToken(user.getEmail()));
            emailService.sendPasswordResetMail(originalUser.get().getEmail(), originalUser.get().getResetPassToken(), locale, request, originalUser.get().getUserName());
            userRepository.save(originalUser.get());

            return "other/successful-password-reset-firststep";
        }
    }

    @RequestMapping(value = "/registration/confirm/{token}")
    public String emailConfirmPage(Model model, @PathVariable String token) {
        if (!userRepository.findByActivationToken(token).isPresent()) {
            return "other/unsuccessful-token-error";
        } else {
            User user = userRepository.findByActivationToken(token).get();
            user.setActive(true);
            user.setActivationToken(null);
            userRepository.save(user);

            return "other/successful-acc-confirm";
        }
    }

    @RequestMapping(value = "/resetPassword/{resetPassToken}")
    public String emailResetPasswordPage(Model model, @PathVariable String resetPassToken) {
        if (!userRepository.findByResetPassToken(resetPassToken).isPresent()) {
            return "other/unsuccessful-token-error";
        } else {
            User user = userRepository.findByResetPassToken(resetPassToken).get();
            model.addAttribute(user);

            return "security/reset-set-password";
        }
    }

    @PostMapping(value = "/resetPassword/{resetPassToken}")
    public String emailResetPassword(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable String resetPassToken, RedirectAttributes redirectAttributes) {

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.password", "Пароли не совпадают!");
        }

        if (bindingResult.hasErrors()) {
            return "security/reset-set-password";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            User originalUser = userRepository.findByResetPassToken(resetPassToken).get();

            originalUser.setPassword(passwordEncoder.encode(user.getPassword()));
            originalUser.setResetPassToken(null);
            userRepository.save(originalUser);

            redirectAttributes.addFlashAttribute("resetOk", "Пароль успешно сброшен!");

            return "redirect:/login";
        }
    }
}