package br.com.leonardoferreira.jirareport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.embedded.LeadTimeBySize;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.repository.IssuePeriodRepository;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Seed implements CommandLineRunner {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private IssuePeriodRepository issuePeriodRepository;

    @Autowired
    private ChartService chartService;

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public void run(String... args) throws Exception {
        Project project = buildProject();
        projectRepository.save(project);

        List<Issue> issues = buildIssues();
        issueRepository.saveAll(issues);

        IssuePeriod issuePeriod = buildIssuePeriod(issues);
        issuePeriodRepository.save(issuePeriod);
    }

    @SneakyThrows
    private IssuePeriod buildIssuePeriod(List<Issue> issues) {
        IssuePeriod issuePeriod = new IssuePeriod();
        issuePeriod.setId(new IssuePeriodId("01/01/2018", "10/01/2018", 1L));
        issuePeriod.setIssues(issues);
        issuePeriod.setAvgLeadTime(5D);

        CompletableFuture<Chart<Long, Long>> histogram = chartService.issueHistogram(issuePeriod.getIssues());
        CompletableFuture<Chart<String, Long>> estimatedChart = chartService.estimatedChart(issuePeriod.getIssues());
        CompletableFuture<Chart<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issuePeriod.getIssues());
        CompletableFuture<Chart<String, Long>> tasksBySystem = chartService.tasksBySystem(issuePeriod.getIssues());
        CompletableFuture<Chart<String, Double>> leadTimeBySize = chartService.leadTimeBySize(issuePeriod.getIssues());
        CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg = chartService.columnTimeAvg(issuePeriod.getIssues());

        issuePeriod.setHistogram(histogram.get());
        issuePeriod.setEstimated(estimatedChart.get());
        issuePeriod.setLeadTimeBySystem(leadTimeBySystem.get());
        issuePeriod.setTasksBySystem(tasksBySystem.get());
        issuePeriod.setLeadTimeBySize(leadTimeBySize.get());
        issuePeriod.setColumnTimeAvgs(columnTimeAvg.get());

        return issuePeriod;
    }

    private List<Issue> buildIssues() {
        List<Issue> issues = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i < 11; i++) {
            Issue issue = new Issue();
            issue.setKey("MOCK-" + i);
            issue.setCreator("Mock Creator");
            issue.setCreated("26/12/1995");
            issue.setUpdated("26/12/1995");
            issue.setStartDate(LocalDate.of(2018, 01, i).format(dateTimeFormatter));
            issue.setEndDate(LocalDate.of(2018, 01, i + 5).format(dateTimeFormatter));
            issue.setLeadTime(5L);
            issue.setSystem(i % 2 == 0 ? "System Mock" : "Other System");
            issue.setEpic("Epic Mock");
            issue.setSummary("Mock description");
            issue.setChangelog(buildChangeLog());
            issue.setEstimated("M");
            issues.add(issue);
        }

        return issues;
    }

    private List<Changelog> buildChangeLog() {
        Changelog anaDev = new Changelog();
        anaDev.setCreated("01/01/2018");
        anaDev.setFrom("ANALISE");
        anaDev.setTo("DEV");
        anaDev.setCycleTime(1L);

        Changelog devTes = new Changelog();
        devTes.setCreated("02/01/2018");
        devTes.setFrom("DEV");
        devTes.setTo("TEST");
        devTes.setCycleTime(2L);

        Changelog tesDon = new Changelog();
        tesDon.setCreated("04/01/2018");
        tesDon.setFrom("TEST");
        tesDon.setTo("DONE");
        tesDon.setCycleTime(2L);

        return Arrays.asList(anaDev, devTes, tesDon);
    }

    private Project buildProject() {
        Project project = new Project();

        project.setId(1L);
        project.setName("Project Mock");
        project.setStartColumn("StartColumn");
        project.setEndColumn("EndColumn");
        project.setEpicCF("EpicCF");
        project.setEstimateCF("EstimateCF");
        project.setSystemCF("SystemCF");

        return project;
    }

}
