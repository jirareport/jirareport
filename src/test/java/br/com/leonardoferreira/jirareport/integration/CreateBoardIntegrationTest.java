package br.com.leonardoferreira.jirareport.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.base.WithDefaultUser;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.extension.LoadStubs;
import br.com.leonardoferreira.jirareport.extension.WireMockExtension;
import br.com.leonardoferreira.jirareport.factory.AccountFactory;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, WireMockExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @LoadStubs
    @WithDefaultUser
    public void renderCreateBoardPage() throws Exception {
        mockMvc
                .perform(
                        get("/boards/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/new"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(model().attribute("projects", Matchers.hasSize(5)));
    }

    @Test
    @WithDefaultUser
    public void createBoard() throws Exception {
        mockMvc
                .perform(
                        post("/boards")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("externalId", "1")
                                .param("name", "project 1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/1/edit"));

        Assertions.assertEquals(1, boardRepository.count());

        Board board = boardRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);
        Assertions.assertAll("board content",
                () -> Assertions.assertEquals(AccountFactory.DEFAULT_USER, board.getOwner()),
                () -> Assertions.assertEquals("project 1", board.getName()),
                () -> Assertions.assertEquals(1L, board.getExternalId().longValue()));
    }

}
