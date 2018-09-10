package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LeadTimeConfigMapper {

    @Mappings({
            @Mapping(target = "id",           ignore = true),
            @Mapping(target = "board",        ignore = true),
            @Mapping(target = "owner",        ignore = true),
            @Mapping(target = "createdAt",    ignore = true),
            @Mapping(target = "updatedAt",    ignore = true),
            @Mapping(target = "startColumns", ignore = true),
            @Mapping(target = "endColumns",   ignore = true),
            @Mapping(target = "lastEditor",   ignore = true)
    })
    void update(@MappingTarget LeadTimeConfig target, LeadTimeConfig source);

}
