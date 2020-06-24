package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sb.project.domain.Users;
import sb.project.repositories.UsersRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping(value = "/successful-login")
    public String successfulLoginPage(Model model) {
        return "successful-login";
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
    public String registrationPage(Model model, @ModelAttribute("user") @Valid Users user, BindingResult bindingResult, @RequestParam("gendername") String gendername) {
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
            if (user.getEmail().equals(exuser.getEmail())) {
                bindingResult.rejectValue("email", "error.email", "Пользователь с такой электронной почтой уже зарегистрирован!");
            }
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setActive(true);
            user.setRoles("ROLE_USER");
            user.setGender(gendername);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            usersRepository.save(user);

            return "successful-registration";
        }

    }
}