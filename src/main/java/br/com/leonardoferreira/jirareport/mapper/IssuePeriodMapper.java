package br.com.leonardoferreira.jirareport.mapper;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author lferreira on 28/05/18
 */
@Mapper(componentModel = "spring")
public interface IssuePeriodMapper {

    @Mappings({
            @Mapping(target = "id",                   ignore = true),
            @Mapping(target = "startDate",            source = "issuePeriodForm.startDate"),
            @Mapping(target = "endDate",              source = "issuePeriodForm.endDate"),
            @Mapping(target = "boardId",              source = "boardId"),
            @Mapping(target = "issues",               source = "issues"),
            @Mapping(target = "avgLeadTime",          source = "avgLeadTime"),
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
            @Mapping(target = "issuesCount",          source = "issueCount"),
            @Mapping(target = "jql",                  source = "jql"),
            @Mapping(target = "wipAvg",               source = "wipAvg"),
            @Mapping(target = "owner",                ignore = true),
            @Mapping(target = "lastEditor",           ignore = true),
            @Mapping(target = "createdAt",            ignore = true),
            @Mapping(target = "updatedAt",            ignore = true)
    })
    IssuePeriod fromJiraData(IssuePeriodForm issuePeriodForm, List<Issue> issues,
                             Double avgLeadTime, ChartAggregator chartAggregator,
                             Integer issueCount, Long boardId, String jql, Double wipAvg);
}
