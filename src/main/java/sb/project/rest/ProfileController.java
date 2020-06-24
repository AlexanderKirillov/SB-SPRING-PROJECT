package sb.project.rest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import sb.project.domain.Categories;
import sb.project.domain.Users;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.UsersRepository;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(value = "/profile")
    public String userItemPage(Model model, @ModelAttribute("ctgSel") Categories ctgSel, Authentication authentication) {
        List<Categories> categoriesList = categoriesRepository.findAll();

        model.addAttribute("categories", categoriesList);

        for (Categories ctg : categoriesList) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        if (authentication != null) {
            Users user = usersRepository.findByUserName(authentication.getName()).get();
            model.addAttribute("user", user);
        }

        return "profile";
    }
}