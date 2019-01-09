package br.com.leonardoferreira.jirareport.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.base.WithDefaultUser;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.factory.AccountFactory;
import br.com.leonardoferreira.jirareport.factory.BoardFactory;
import java.util.Map;
import java.util.Objects;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Test
    @WithDefaultUser
    public void findAllCurrentUserBoards() throws Exception {
        boardFactory.create(10);
        withUser("other_user", () -> boardFactory.create(5));

        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/index"))
                .andExpect(model().attributeExists("boards", "board", "owners"))
                .andExpect(model().attribute("owners", Matchers.containsInAnyOrder("default_user", "other_user")))
                .andExpect(model().attribute("board", Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.isEmptyOrNullString()),
                        Matchers.hasProperty("owner", Matchers.is(AccountFactory.DEFAULT_USER))
                )))
                .andExpect(model().attribute("boards", Matchers.allOf(
                        Matchers.hasProperty("totalPages", Matchers.is(1)),
                        Matchers.hasProperty("totalElements", Matchers.is(10L)),
                        Matchers.hasProperty("content", Matchers.hasSize(10))
                )));
    }

    @Test
    @WithDefaultUser
    public void filterBoardByName() throws Exception {
        boardFactory.create(5, empty -> {
            empty.setName("Uniq Start Name");
        });
        boardFactory.create(5);

        MvcResult mvcResult = mockMvc
                .perform(
                        get("/boards")
                                .param("name", "start"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/index"))
                .andExpect(model().attributeExists("boards", "board", "owners"))
                .andExpect(model().attribute("owners", Matchers.contains(AccountFactory.DEFAULT_USER)))
                .andExpect(model().attribute("board", Matchers.allOf(
                        Matchers.hasProperty("owner", Matchers.is(AccountFactory.DEFAULT_USER)),
                        Matchers.hasProperty("name", Matchers.is("start"))
                )))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        Page<Board> boards = (Page<Board>) model.get("boards");
        Assertions.assertAll("boards content",
                () -> Assertions.assertEquals(boards.getTotalPages(), 1),
                () -> Assertions.assertEquals(boards.getTotalElements(), 5),
                () -> Assertions.assertEquals(5, boards.getContent().stream()
                        .filter(board -> board.getName().equals("Uniq Start Name")).count()));

    }

    @Test
    @WithDefaultUser
    public void findBoardsOfAllOwners() throws Exception {
        boardFactory.create(5);
        withUser("user2", () -> boardFactory.create(5));
        withUser("user3", () -> boardFactory.create(5));
        withUser("user4", () -> boardFactory.create(5));

        MvcResult mvcResult = mockMvc
                .perform(get("/boards")
                        .param("owner", "all"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/index"))
                .andExpect(model().attributeExists("boards", "board", "owners"))
                .andExpect(model().attribute("owners", Matchers.contains(
                        AccountFactory.DEFAULT_USER, "user2", "user3", "user4")))
                .andExpect(model().attribute("board", Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.isEmptyOrNullString()),
                        Matchers.hasProperty("owner", Matchers.nullValue())
                )))
                .andExpect(model().attribute("boards", Matchers.allOf(
                        Matchers.hasProperty("totalPages", Matchers.is(1)),
                        Matchers.hasProperty("totalElements", Matchers.is(20L)),
                        Matchers.hasProperty("content", Matchers.hasSize(20))
                )))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        Page<Board> boards = (Page<Board>) model.get("boards");
        Assertions.assertAll("boards content",
                () -> Assertions.assertEquals(boards.getTotalPages(), 1),
                () -> Assertions.assertEquals(boards.getTotalElements(), 20),
                () -> Assertions.assertEquals(5, boards.getContent().stream()
                        .filter(board -> board.getOwner().equals(AccountFactory.DEFAULT_USER)).count()),
                () -> Assertions.assertEquals(5, boards.getContent().stream()
                        .filter(board -> board.getOwner().equals("user2")).count()),
                () -> Assertions.assertEquals(5, boards.getContent().stream()
                        .filter(board -> board.getOwner().equals("user3")).count()),
                () -> Assertions.assertEquals(5, boards.getContent().stream()
                        .filter(board -> board.getOwner().equals("user4")).count()));
    }
}
