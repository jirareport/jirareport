package br.com.leonardoferreira.jirareport.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.base.WithDefaultUser;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.extension.LoadStubs;
import br.com.leonardoferreira.jirareport.extension.WireMockExtension;
import br.com.leonardoferreira.jirareport.factory.BoardFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, WireMockExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Test
    @LoadStubs
    @WithDefaultUser
    public void renderEdit() throws Exception {
        Board board = boardFactory.create(empty -> {
            empty.setExternalId(1L);
        });

        mockMvc
                .perform(
                        get("/boards/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/edit"))
                .andExpect(model().attributeExists("boardForm", "suggestedStatus", "jiraFields"))
                .andExpect(model().attribute("suggestedStatus", Matchers.hasSize(5)))
                .andExpect(model().attribute("jiraFields", Matchers.hasSize(5)))
                .andExpect(model().attribute("boardForm", Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.is(board.getName())),
                        Matchers.hasProperty("jiraProject", Matchers.allOf(
                                Matchers.hasProperty("id", Matchers.is(1L)),
                                Matchers.hasProperty("name", Matchers.is("project 1")),
                                Matchers.hasProperty("issueTypes", Matchers.hasSize(5))
                        ))
                )));
    }
}
