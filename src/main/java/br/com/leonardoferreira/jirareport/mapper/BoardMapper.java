package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 * @author lferreira on 31/05/18
 */
@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mappings({
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
    })
    BoardForm toForm(Board board, JiraProject jiraProject);

    @Mappings({
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "externalId",      ignore = true),
            @Mapping(target = "leadTimeConfigs", ignore = true),
            @Mapping(target = "holidays",        ignore = true),
            @Mapping(target = "issues",          ignore = true),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    })
    void fromForm(@MappingTarget Board board, BoardForm boardForm);
}
