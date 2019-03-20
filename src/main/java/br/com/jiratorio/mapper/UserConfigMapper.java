package br.com.jiratorio.mapper;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.jiratorio.domain.response.UserConfigResponse;
import br.com.jiratorio.mapper.transformer.CityTransformer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        uses = CityTransformer.class)
public interface UserConfigMapper {

    UserConfigResponse userConfigToResponse(UserConfig userConfig);

    @Mappings({ // @formatter:off
            @Mapping(target = "id",         ignore = true),
            @Mapping(target = "city",       qualifiedByName = "normalizeCity"),
            @Mapping(target = "updatedAt",  ignore = true),
            @Mapping(target = "createdAt",  ignore = true),
            @Mapping(target = "owner",      ignore = true),
            @Mapping(target = "lastEditor", ignore = true),
            @Mapping(target = "username",   ignore = true),
    }) // @formatter:on
    void updateFromRequest(@MappingTarget UserConfig userConfig, UpdateUserConfigRequest updateUserConfigRequest);

    default ImportHolidayInfo toImportHolidayInfo(UserConfig userConfig) {
        return new ImportHolidayInfo(userConfig.getState(), userConfig.getCity(), userConfig.getHolidayToken());
    }

}
