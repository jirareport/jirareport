package br.com.jiratorio.service.impl;

import br.com.jiratorio.context.UnitTestContext;
import br.com.jiratorio.domain.DueDateType;
import br.com.jiratorio.domain.embedded.DueDateHistory;
import br.com.jiratorio.domain.vo.changelog.JiraChangelogItem;
import br.com.jiratorio.factory.domain.JiraChangelogItemFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestContext.class)
public class DueDateServiceImplTest {

    private DueDateServiceImpl dueDateService;

    private final JiraChangelogItemFactory jiraChangelogItemFactory;

    @Autowired
    public DueDateServiceImplTest(final JiraChangelogItemFactory jiraChangelogItemFactory) {
        this.jiraChangelogItemFactory = jiraChangelogItemFactory;
    }

    @BeforeEach
    public void setUp() {
        dueDateService = new DueDateServiceImpl();
    }

    @Test
    public void testExtractDueDateHistoryWithOneItem() {
        JiraChangelogItem jiraChangelogItem = jiraChangelogItemFactory.build();

        List<DueDateHistory> dueDateHistories = dueDateService
                .extractDueDateHistory("duedate", Collections.singletonList(jiraChangelogItem));

        Assertions.assertThat(dueDateHistories)
                .hasSize(1)
                .first()
                .extracting(
                        DueDateHistory::getDueDate,
                        DueDateHistory::getCreated
                )
                .containsExactly(
                        LocalDate.parse(jiraChangelogItem.getTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        jiraChangelogItem.getCreated()
                );
    }

    @Test
    public void testExtractDueDateHistoryWithManyItems() {
        jiraChangelogItemFactory.build(5, empty -> empty.setField("other_field"));
        List<JiraChangelogItem> jiraChangelogItems = jiraChangelogItemFactory.build(5);

        List<DueDateHistory> dueDateHistories = dueDateService.extractDueDateHistory("duedate", jiraChangelogItems);

        Assertions.assertThat(dueDateHistories)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparing(DueDateHistory::getCreated));
    }

    @Test
    public void testCalcDeviationOfEstimateWithEmptyHistory() {
        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(null, null, null, null, null);
        Assertions.assertThat(deviationOfEstimate)
                .isNull();
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeFirstAndLastDueDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        DueDateHistory thirdDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 15));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate, thirdDueDate);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, null, DueDateType.FIRST_AND_LAST_DUE_DATE, true, null);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(11);
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeFirstAndEndDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate);
        LocalDateTime endDate = LocalDateTime.of(2019, 2, 13, 13, 30);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.FIRST_DUE_DATE_AND_END_DATE, true, null);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(9);
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeLastAndEndDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate);
        LocalDateTime endDate = LocalDateTime.of(2019, 2, 15, 9, 40);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.LAST_DUE_DATE_AND_END_DATE, true, null);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(6);
    }
}