package br.com.leonardoferreira.jirareport.migration;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.repository.IssuePeriodRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AddJqlInAllIssuePeriods implements ApplicationRunner {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssuePeriodRepository issuePeriodRepository;

    @Autowired
    private BoardService boardService;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        log.info("Method=run, Info=Adicionando JQL nas issues periods criadas antes da JIRAT-65");

        try {
            List<IssuePeriod> issuePeriods = issuePeriodRepository.findByJqlIsNull();
            for (IssuePeriod issuePeriod : issuePeriods) {
                IssuePeriodForm issuePeriodForm = new IssuePeriodForm(issuePeriod.getStartDate(), issuePeriod.getEndDate());
                Board board = boardService.findById(issuePeriod.getBoardId());
                String jql = issueService.searchJQL(issuePeriodForm, board);
                issuePeriod.setJql(jql);

                issuePeriodRepository.save(issuePeriod);
            }
        } catch (Exception e) {
            log.error("Method=run, e={}", e.getMessage(), e);
        }
    }
}
