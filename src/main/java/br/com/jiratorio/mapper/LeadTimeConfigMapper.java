package br.com.jiratorio.mapper;

import br.com.jiratorio.domain.LeadTimeConfig;
import br.com.jiratorio.domain.request.LeadTimeConfigRequest;
import br.com.jiratorio.domain.response.LeadTimeConfigResponse;
import br.com.jiratorio.mapper.util.StringTransformer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = StringTransformer.class)
public interface LeadTimeConfigMapper {

    @Mappings({ // @formatter:off
            @Mapping(target = "boardId", source = "board.id")
    }) // @formatter:on
    LeadTimeConfigResponse toResponse(LeadTimeConfig leadTimeConfig);

    List<LeadTimeConfigResponse> toResponse(List<LeadTimeConfig> leadTimeConfigs);

    @Mappings({ // @formatter:off
            @Mapping(target = "id",           ignore = true),
            @Mapping(target = "board.id",     source = "boardId"),
            @Mapping(target = "name",         source = "request.name"),
            @Mapping(target = "startColumn",  source = "request.startColumn", qualifiedByName = "toUpperCase"),
            @Mapping(target = "endColumn",    source = "request.endColumn",   qualifiedByName = "toUpperCase"),
            @Mapping(target = "owner",        ignore = true),
            @Mapping(target = "lastEditor",   ignore = true),
            @Mapping(target = "createdAt",    ignore = true),
            @Mapping(target = "updatedAt",    ignore = true),
    }) // @formatter:on
    LeadTimeConfig toLeadTimeConfig(LeadTimeConfigRequest request, Long boardId);

    @Mappings({ // @formatter:off
            @Mapping(target = "id",           ignore = true),
            @Mapping(target = "board.id",     ignore = true),
            @Mapping(target = "startColumn",  qualifiedByName = "toUpperCase"),
            @Mapping(target = "endColumn",    qualifiedByName = "toUpperCase"),
            @Mapping(target = "owner",        ignore = true),
            @Mapping(target = "lastEditor",   ignore = true),
            @Mapping(target = "createdAt",    ignore = true),
            @Mapping(target = "updatedAt",    ignore = true),
    }) // @formatter:on
    void updateFromRequest(@MappingTarget LeadTimeConfig leadTimeConfig, LeadTimeConfigRequest request);
}
