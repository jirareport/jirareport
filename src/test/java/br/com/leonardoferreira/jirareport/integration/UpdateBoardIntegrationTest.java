package br.com.leonardoferreira.jirareport.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.base.WithDefaultUser;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.vo.DynamicFieldConfig;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.extension.LoadStubs;
import br.com.leonardoferreira.jirareport.extension.WireMockExtension;
import br.com.leonardoferreira.jirareport.factory.BoardFactory;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import org.hamcrest.MatcherAssert;
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
public class UpdateBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private BoardRepository boardRepository;

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

    @Test
    @WithDefaultUser
    public void updateBoard() throws Exception {
        boardFactory.create();

        mockMvc
                .perform(
                        put("/boards")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
                                .param("name", "New Name")
                                .param("startColumn", "startColumn")
                                .param("endColumn", "endColumn")
                                .param("fluxColumn", "first", "second", "third")
                                .param("ignoreIssueType", "issue1", "issue2")
                                .param("epicCF", "epicCF")
                                .param("estimateCF", "estimateCF")
                                .param("systemCF", "systemCF")
                                .param("projectCF", "projectCF")
                                .param("calcDueDate", "true")
                                .param("ignoreWeekend", "true")
                                .param("impedimentType", "COLUMN")
                                .param("impedimentColumns", "iclmn1", "iclmn2")
                                .param("dynamicFields[0].name", "dn1Name")
                                .param("dynamicFields[0].field", "dn1Field")
                                .param("dynamicFields[2].name", "dn2Name")
                                .param("dynamicFields[2].field", "dn2Field"))
                .andDo(print())
                .andExpect(redirectedUrl("/boards"))
                .andExpect(flash().attribute("flashSuccess", "Alterações salvas com sucesso."));

        Board board = boardRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);
        Assertions.assertAll("boardContent",
                () -> Assertions.assertEquals("New Name", board.getName()),
                () -> Assertions.assertEquals("STARTCOLUMN", board.getStartColumn()),
                () -> Assertions.assertEquals("ENDCOLUMN", board.getEndColumn()),
                () -> Assertions.assertEquals(3, board.getFluxColumn().size()),
                () -> MatcherAssert.assertThat(board.getFluxColumn(), Matchers.hasItems("FIRST", "SECOND", "THIRD")),
                () -> Assertions.assertEquals(2, board.getIgnoreIssueType().size()),
                () -> MatcherAssert.assertThat(board.getIgnoreIssueType(), Matchers.hasItems("issue1", "issue2")),
                () -> Assertions.assertEquals("epicCF", board.getEpicCF()),
                () -> Assertions.assertEquals("estimateCF", board.getEstimateCF()),
                () -> Assertions.assertEquals("systemCF", board.getSystemCF()),
                () -> Assertions.assertEquals("projectCF", board.getProjectCF()),
                () -> Assertions.assertTrue(board.getCalcDueDate()),
                () -> Assertions.assertTrue(board.getIgnoreWeekend()),
                () -> Assertions.assertEquals(2, board.getImpedimentColumns().size()),
                () -> MatcherAssert.assertThat(board.getImpedimentColumns(), Matchers.hasItems("iclmn1", "iclmn2")),
                () -> Assertions.assertEquals(2, board.getDynamicFields().size()),
                () -> MatcherAssert.assertThat(board.getDynamicFields(), Matchers.hasItems(
                        new DynamicFieldConfig("dn1Name", "dn1Field"),
                        new DynamicFieldConfig("dn2Name", "dn2Field")
                )));
    }

}
