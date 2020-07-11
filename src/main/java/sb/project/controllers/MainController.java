package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Category;
import sb.project.domain.Comment;
import sb.project.domain.Item;
import sb.project.domain.User;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.CommentRepository;
import sb.project.repositories.ItemRepository;
import sb.project.repositories.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/main")
    public String userMainPage(Model model, @RequestParam(value = "selcat", required = false) Long selcat, @ModelAttribute("ctgSel") Category ctgSel) {
        setCtgMenu(model);

        List<Item> itemsList;
        Set<Item> activeItems = new LinkedHashSet<Item>();

        if (selcat == null) {
            itemsList = itemRepository.findAll();
            for (Item item : itemsList) {
                if (item.getCategory().getStatus() && item.getStatus()) {
                    activeItems.add(item);
                }
            }
            model.addAttribute("currentCategory", "-");
        } else {
            itemsList = categoryRepository.findById(selcat).get().getItems();
            for (Item item : itemsList) {
                if (item.getStatus()) {
                    activeItems.add(item);
                }
            }
            model.addAttribute("currentCategory", categoryRepository.findById(selcat).get().getName());
        }

        model.addAttribute("items", activeItems);

        for (Item item : itemsList) {
            byte[] image = item.getImage();
            item.setImageString(Base64.encodeBase64String(image));
        }

        return "main/user-main";
    }

    @GetMapping(value = "/main/items/{itemId}")
    public String userItemPage(Model model, @PathVariable long itemId, @ModelAttribute("ctgSel") Category ctgSel) {
        float totalRating = 0;
        long scoreQuantity = 0;
        setCtgMenu(model);

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        byte[] image = item.getImage();
        item.setImageString(Base64.encodeBase64String(image));

        List<Comment> listComment = commentRepository.findAllByCmtGoodsOrderByDateTimeCommentDesc(item);
        model.addAttribute("comments", listComment);

        Comment comment = new Comment();
        model.addAttribute("cmnt", comment);

        List<Comment> lstComment = commentRepository.findAllByCmtGoods(item);
        for (Comment cmnt : lstComment) {
            if (cmnt.getRating() > 0) {
                totalRating += cmnt.getRating();
                scoreQuantity++;
            }

        }
        totalRating = totalRating / scoreQuantity;

        model.addAttribute("totalRating", Math.round(totalRating * 100.0) / 100.0);
        model.addAttribute("scoreQuantity", scoreQuantity);

        return "main/user-main-item";
    }

    @PostMapping(value = {"/main/items/{itemId}/addComment"})
    public String addComment(Model model, @PathVariable long itemId, @ModelAttribute("cmnt") Comment cmnt, @RequestParam("ratingAdd") String ratingAdd, Authentication authentication) throws IOException, ParseException {
        cmnt.setCmtGoods(itemRepository.findById(itemId));
        if ((ratingAdd != null) && !ratingAdd.isEmpty()) {
            cmnt.setRating(Float.parseFloat(ratingAdd));
        } else {
            cmnt.setRating(0);
        }

        cmnt.setCommentUser(userRepository.findByUserName(authentication.getName()).get());
        cmnt.setDateTimeComment(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(cmnt);

        return "redirect:/main/items/{itemId}";
    }

    @PostMapping(value = "/main/items/{itemId}/comments/{commentId}/delete")
    public String deleteComment(Model model, @PathVariable long itemId, @PathVariable Long commentId, Authentication authentication) {
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = userRepository.findByUserName(authentication.getName()).get();

        if (currentUser.getUserName() == commentRepository.findById(commentId).get().getCommentUser().getUserName() || hasAdminRole) {
            commentRepository.deleteById(commentId);
        }

        return "redirect:/main/items/{itemId}";
    }

    @PostMapping(value = {"/main/items/{itemId}/comments/{commentId}/edit"})
    public String commentEdit(Model model, @ModelAttribute("comment") Comment cmntE, @PathVariable long itemId, @PathVariable Long commentId, @RequestParam("commentText") String commentText, @RequestParam("ratingEdit") String ratingEdit, @RequestParam("commentOriginalDate") String commentOriginalDate, Authentication authentication) throws IOException, ParseException {
        User currentUser = userRepository.findByUserName(authentication.getName()).get();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        cmntE.setId(commentId);
        cmntE.setCommentText(commentText);
        cmntE.setDateTimeComment(format.parse(commentOriginalDate));
        cmntE.setCmtGoods(itemRepository.findById(itemId));
        cmntE.setCommentUser(currentUser);
        if ((ratingEdit != null) && !ratingEdit.isEmpty()) {
            cmntE.setRating(Float.parseFloat(ratingEdit));
        } else {
            cmntE.setRating(0);
        }

        commentRepository.save(cmntE);

        return "redirect:/main/items/{itemId}";
    }

    public void setCtgMenu(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> activeCategories = new ArrayList<Category>();

        for (Category ctg : categoryList) {
            if (ctg.getStatus()) {
                activeCategories.add(ctg);
            }
        }

        for (Category ctg : activeCategories) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        model.addAttribute("categories", activeCategories);
    }
}