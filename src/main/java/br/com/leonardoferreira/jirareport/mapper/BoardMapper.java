package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import br.com.leonardoferreira.jirareport.domain.request.CreateBoardRequest;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {

    @Mappings({ // @formatter:off
            @Mapping(target = "id",              source = "board.id"),
            @Mapping(target = "name",            source = "board.name"),
            @Mapping(target = "startColumn",     source = "board.startColumn"),
            @Mapping(target = "endColumn",       source = "board.endColumn"),
            @Mapping(target = "fluxColumn",      source = "board.fluxColumn"),
            @Mapping(target = "ignoreIssueType", source = "board.ignoreIssueType"),
            @Mapping(target = "epicCF",          source = "board.epicCF"),
            @Mapping(target = "estimateCF",      source = "board.estimateCF"),
            @Mapping(target = "systemCF",        source = "board.systemCF"),
            @Mapping(target = "projectCF",       source = "board.projectCF"),
            @Mapping(target = "dynamicFields",   source = "board.dynamicFields"),
            @Mapping(target = "jiraProject",     source = "jiraProject")
    }) // @formatter:on
    BoardForm toForm(Board board, JiraProject jiraProject);

    void fromForm(@MappingTarget Board board, BoardForm boardForm);

    Board boardFromCreateBoardRequest(CreateBoardRequest request);

}
