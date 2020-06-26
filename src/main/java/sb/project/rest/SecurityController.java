package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Users;
import sb.project.repositories.UsersRepository;
import sb.project.services.EmailService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        //emailService.sendMail("nowenui@bk.ru", "Test Subject", "Test mail");
        return "login";
    }

    @GetMapping(value = "/access-error")
    public String accessErrorPage(Model model) {
        return "access-denied-page";
    }

    @GetMapping(value = {"/registration"})
    public String registrationPage(Model model) {
        Users user = new Users();

        model.addAttribute("user", user);

        return "registration";
    }

    @PostMapping(value = {"/registration"})
    public String registrationPage(Model model, @ModelAttribute("user") @Valid Users user, BindingResult bindingResult, @RequestParam("gendername") String gendername) throws Exception {
        List<Users> userList = usersRepository.findAll();

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.password", "Пароли не совпадают!");
        }

        for (Users exuser : userList) {
            if (user.getUserName().equals(exuser.getUserName())) {
                bindingResult.rejectValue("userName", "error.userName", "Пользователь с таким никнейном уже зарегистрирован!");
            }
        }

        for (Users exuser : userList) {
            if (user.getEmail().toLowerCase().equals(exuser.getEmail().toLowerCase())) {
                bindingResult.rejectValue("email", "error.email", "Пользователь с такой электронной почтой уже зарегистрирован!");
            }
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setActive(false);
            user.setEmail(user.getEmail().toLowerCase());
            user.setRoles("ROLE_USER");
            user.setGender(gendername);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setToken(emailService.generateToken(user.getUserName()));
            emailService.sendConfirmationMail(user.getEmail(), user.getUserName(), "http://localhost:8080/users/confirm/" + user.getToken());

            usersRepository.save(user);

            return "successful-registration";
        }
    }

    @RequestMapping(value = "/users/confirm/{token}")
    public String emailConfirmPage(Model model, @PathVariable String token) {
        if (usersRepository.findByToken(token).isEmpty()) {
            return "unsuccessful-acc-confirm";
        } else {
            Users user = usersRepository.findByToken(token).get();
            user.setActive(true);
            user.setToken(null);
            usersRepository.save(user);

            return "successful-acc-confirm";
        }
    }
}