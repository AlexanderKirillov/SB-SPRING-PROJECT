package sb.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminBaseController {

    @GetMapping(value = "/admin")
    public String adminMainPage(Model model) {
        return "admin/admin-base";
    }
}
