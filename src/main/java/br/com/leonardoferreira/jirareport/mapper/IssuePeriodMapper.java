package br.com.leonardoferreira.jirareport.mapper;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IssuePeriodMapper {

    @Mappings({
            @Mapping(target = "id",                   ignore = true),
            @Mapping(target = "startDate",            source = "issuePeriodForm.startDate"),
            @Mapping(target = "endDate",              source = "issuePeriodForm.endDate"),
            @Mapping(target = "boardId",              source = "details.boardId"),
            @Mapping(target = "issues",               source = "issues"),
            @Mapping(target = "avgLeadTime",          source = "details.avgLeadTime"),
            @Mapping(target = "histogram",            source = "chartAggregator.histogram"),
            @Mapping(target = "estimated",            source = "chartAggregator.estimated"),
            @Mapping(target = "leadTimeBySystem",     source = "chartAggregator.leadTimeBySystem"),
            @Mapping(target = "tasksBySystem",        source = "chartAggregator.tasksBySystem"),
            @Mapping(target = "leadTimeBySize",       source = "chartAggregator.leadTimeBySize"),
            @Mapping(target = "columnTimeAvgs",       source = "chartAggregator.columnTimeAvg"),
            @Mapping(target = "leadTimeByType",       source = "chartAggregator.leadTimeByType"),
            @Mapping(target = "tasksByType",          source = "chartAggregator.tasksByType"),
            @Mapping(target = "leadTimeByProject",    source = "chartAggregator.leadTimeByProject"),
            @Mapping(target = "tasksByProject",       source = "chartAggregator.tasksByProject"),
            @Mapping(target = "leadTimeCompareChart", source = "chartAggregator.leadTimeCompareChart"),
            @Mapping(target = "leadTimeByPriority",   source = "chartAggregator.leadTimeByPriority"),
            @Mapping(target = "throughputByPriority", source = "chartAggregator.throughputByPriority"),
            @Mapping(target = "dynamicCharts",        source = "chartAggregator.dynamicCharts"),
            @Mapping(target = "issuesCount",          source = "details.issueCount"),
            @Mapping(target = "jql",                  source = "details.jql"),
            @Mapping(target = "wipAvg",               source = "details.wipAvg"),
            @Mapping(target = "avgPctEfficiency",     source = "details.avgPctEfficiency"),
            @Mapping(target = "owner",                ignore = true),
            @Mapping(target = "lastEditor",           ignore = true),
            @Mapping(target = "createdAt",            ignore = true),
            @Mapping(target = "updatedAt",            ignore = true)
    })
    IssuePeriod fromJiraData(IssuePeriodForm issuePeriodForm, List<Issue> issues,
                             ChartAggregator chartAggregator, IssuePeriodDetails details);
}
