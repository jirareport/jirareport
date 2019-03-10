package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.entity.Board;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class JQLServiceImplTest {

    private JQLServiceImpl jqlService;

    private Board board;

    @BeforeEach
    void setUp() {
        jqlService = new JQLServiceImpl();
        board = new Board();
        board.setExternalId(123123L);
        board.setStartColumn("TODO");
        board.setEndColumn("DONE");
        board.setFluxColumn(Arrays.asList("TODO", "WIP", "DONE"));
    }

    @Nested
    class FinalizedIssues {

        private LocalDate startDate;

        private LocalDate endDate;

        @BeforeEach
        void setUp() {
            startDate = LocalDate.of(2019, 1, 1);
            endDate = LocalDate.of(2019, 1, 31);
        }

        @Test
        void testFinalizedIssuesWithIgnoreIssueType() {
            board.setIgnoreIssueType(Collections.singletonList("IT_1"));

            String jql = jqlService.finalizedIssues(board, startDate, endDate);

            Assertions.assertThat(jql)
                    .contains("project = '123123'")
                    .contains("STATUS CHANGED TO 'DONE' DURING('2019-01-01', '2019-01-31 23:59')")
                    .contains("OR ( STATUS CHANGED TO 'DONE' DURING ('2019-01-01', '2019-01-31 23:59') AND NOT STATUS CHANGED TO 'DONE' )")
                    .contains("OR ( resolutiondate >= '2019-01-01' AND resolutiondate <= '2019-01-31 23:59'")
                    .contains("AND NOT STATUS CHANGED TO 'DONE' AND NOT STATUS CHANGED TO 'DONE')")
                    .contains("AND issueType NOT IN ('IT_1')")
                    .contains("AND status WAS IN ('DONE','TODO','WIP')  AND status IN ('DONE')");
        }

        @Test
        void testFinalizedIssuesWithoutIgnoreIssueType() {
            String jql = jqlService.finalizedIssues(board, startDate, endDate);

            Assertions.assertThat(jql)
                    .contains("project = '123123'")
                    .contains("STATUS CHANGED TO 'DONE' DURING('2019-01-01', '2019-01-31 23:59')")
                    .contains("OR ( STATUS CHANGED TO 'DONE' DURING ('2019-01-01', '2019-01-31 23:59') AND NOT STATUS CHANGED TO 'DONE' )")
                    .contains("OR ( resolutiondate >= '2019-01-01' AND resolutiondate <= '2019-01-31 23:59'")
                    .contains("AND NOT STATUS CHANGED TO 'DONE' AND NOT STATUS CHANGED TO 'DONE')")
                    .contains("AND status WAS IN ('DONE','TODO','WIP')  AND status IN ('DONE')");
        }

    }

    @Nested
    class OpenedIssue {

        @Test
        void testOpenedIssuesWithIgnoreIssueType() {
            board.setIgnoreIssueType(Collections.singletonList("IT_1"));

            String jql = jqlService.openedIssues(board);

            Assertions.assertThat(jql)
                    .contains("project = '123123'")
                    .contains("AND issueType NOT IN ('IT_1')")
                    .contains("AND status IN ('TODO','WIP')");
        }

        @Test
        void testOpenedIssuesWithoutIgnoreIssueType() {
            String jql = jqlService.openedIssues(board);

            Assertions.assertThat(jql)
                    .contains("project = '123123'")
                    .contains("AND status IN ('TODO','WIP')");
        }

    }

}
