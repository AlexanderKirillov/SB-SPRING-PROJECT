package sb.project.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sb.project.domain.Category;
import sb.project.repositories.CategoryRepository;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminCategoriesController.class)
@AutoConfigureMockMvc(addFilters = false)
@OverrideAutoConfiguration(enabled = true)
public class AdminCategoriesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void adminCategoriesPageTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/categories"))
                .andExpect(model().size(3))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void adminDeleteCategoryTest() throws Exception {
        Category ctg = new Category();
        ctg.setName("TestCategoryName");
        ctg.setDescription("TestCategoryDescription");
        ctg.setStatus(true);
        ctg.setImage(null);
        ctg.setItems(null);
        categoryRepository.save(ctg);

        mvc.perform(MockMvcRequestBuilders.post("/admin/categories/{categoryId}/delete", ctg.getId()))
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Transactional
    public void adminEditCategoryTest() throws Exception {
        Category ctg = new Category();
        ctg.setName("TestCategoryName");
        ctg.setDescription("TestCategoryDescription");
        ctg.setStatus(true);
        ctg.setImage(null);
        ctg.setItems(null);
        categoryRepository.save(ctg);

        MockMultipartFile img = new MockMultipartFile("img",
                "img", "text/plain", ctg.getName().getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/admin/categories/{categoryId}/edit", ctg.getId()).file(img).param("name", ctg.getName()).param("description", ctg.getDescription()))
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Transactional
    public void adminAddCategoryTest() throws Exception {
        Category ctg = new Category();
        ctg.setName("TestCategoryName");
        ctg.setDescription("TestCategoryDescription");
        ctg.setStatus(true);
        ctg.setImage(null);
        ctg.setItems(null);
        categoryRepository.save(ctg);

        MockMultipartFile img = new MockMultipartFile("img",
                "img", "text/plain", ctg.getName().getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/admin/categories/add").file(img))
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection());
    }

}
