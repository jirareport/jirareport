package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 * @author lferreira on 31/05/18
 */
@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardForm toForm(Board board);

    @Mappings({
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "externalId",      ignore = true),
            @Mapping(target = "leadTimeConfigs", ignore = true),
            @Mapping(target = "holidays",        ignore = true),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    })
    void fromForm(@MappingTarget Board board, BoardForm boardForm);
}
