package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Categories;
import sb.project.domain.Users;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.UsersRepository;
import sb.project.services.UserDetailsImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(value = "/profile")
    public String profilePage(Model model, @ModelAttribute("ctgSel") Categories ctgSel, Authentication authentication) {
        setCtgMenu(model);

        if (authentication != null) {
            Users user = usersRepository.findByUserName(authentication.getName()).get();
            model.addAttribute("user", user);
        }

        return "profile";
    }

    @GetMapping(value = {"/users/{profileId}/edit"})
    public String userProfileEditPage(Model model, @PathVariable long profileId) {
        Users user = usersRepository.findById(profileId).get();

        model.addAttribute("user", user);
        setCtgMenu(model);

        return "profile-edit";
    }

    @PostMapping(value = {"/users/{profileId}/edit"})
    public String userProfileEdit(Model model, @PathVariable long profileId,
                                  @ModelAttribute("user") @Valid Users user, BindingResult bindingResult,
                                  @RequestParam("gendername") String gendername, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        setCtgMenu(model);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<Users> userList = usersRepository.findAll();
        Users thisUser = usersRepository.findById(profileId).get();

        for (int i = 0; i < userList.size(); i++) {
            Users usr = userList.get(i);
            if (usr.getId() == profileId) {
                userList.remove(i);
            }
        }

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
            return "profile-edit";
        } else {
            user.setId(profileId);
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(thisUser.getRoles());
            user.setGender(gendername);

            usersRepository.save(user);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userDetails.setUsername(user.getUserName());
            userDetails.setPassword(user.getPassword());

            return "redirect:/profile";
        }
    }

    public void setCtgMenu(Model model) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        List<Categories> activeCategories = new ArrayList<Categories>();

        for (Categories ctg : categoriesList) {
            if (ctg.getStatus()) {
                activeCategories.add(ctg);
            }
        }

        for (Categories ctg : activeCategories) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        model.addAttribute("categories", activeCategories);
    }

}