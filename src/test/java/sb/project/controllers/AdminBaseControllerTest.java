package sb.project.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminBaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminBaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void adminMainPageTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(model().size(0))
                .andExpect(status().isOk());
    }
}
