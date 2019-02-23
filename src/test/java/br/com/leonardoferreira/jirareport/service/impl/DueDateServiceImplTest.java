package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.context.UnitTestContext;
import br.com.leonardoferreira.jirareport.domain.DueDateType;
import br.com.leonardoferreira.jirareport.domain.embedded.DueDateHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import br.com.leonardoferreira.jirareport.factory.JiraChangelogItemFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = UnitTestContext.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class DueDateServiceImplTest {

    @InjectMocks
    private DueDateServiceImpl dueDateService;

    @Autowired
    private JiraChangelogItemFactory jiraChangelogItemFactory;

    @Test
    public void testExtractDueDateHistoryWithOneItem() {
        JiraChangelogItem jiraChangelogItem = jiraChangelogItemFactory.build();

        List<DueDateHistory> dueDateHistories = dueDateService
                .extractDueDateHistory("duedate", Collections.singletonList(jiraChangelogItem));

        ObjectAssert<DueDateHistory> firstDueDate = Assertions.assertThat(dueDateHistories)
                .hasSize(1)
                .first();

        firstDueDate.extracting(DueDateHistory::getDueDate)
                .isNotNull()
                .isEqualTo(LocalDate.parse(jiraChangelogItem.getTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        firstDueDate.extracting(DueDateHistory::getCreated)
                .isNotNull()
                .isEqualTo(jiraChangelogItem.getCreated());
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
        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(null, null, null);
        Assertions.assertThat(deviationOfEstimate)
                .isNull();
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeFirstAndLastDueDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        DueDateHistory thirdDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 15));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate, thirdDueDate);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, null, DueDateType.FIRST_AND_LAST_DUE_DATE);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(10);
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeFirstAndEndDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate);
        LocalDateTime endDate = LocalDateTime.of(2019, 2, 13, 13, 30);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate, DueDateType.FIRST_DUE_DATE_AND_END_DATE);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(8);
    }

    @Test
    public void testCalcDeviationOfEstimateWithDueDateTypeLastAndEndDate() {
        DueDateHistory firstDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 5));
        DueDateHistory secondDueDate = new DueDateHistory(null, LocalDate.of(2019, 2, 10));
        List<DueDateHistory> dueDateHistories = Arrays.asList(firstDueDate, secondDueDate);
        LocalDateTime endDate = LocalDateTime.of(2019, 2, 15, 9, 40);

        Long deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate, DueDateType.LAST_DUE_DATE_AND_END_DATE);

        Assertions.assertThat(deviationOfEstimate)
                .isEqualTo(5);
    }
}