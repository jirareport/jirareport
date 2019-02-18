package br.com.jiratorio.mapper;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.UserConfig;
import br.com.jiratorio.domain.form.UserConfigForm;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserConfigMapper {

    UserConfigForm userConfigToForm(UserConfig userConfig);

    @Mappings({ // @formatter:off
            @Mapping(target = "id",         ignore = true),
            @Mapping(target = "updatedAt",  ignore = true),
            @Mapping(target = "createdAt",  ignore = true),
            @Mapping(target = "owner",      ignore = true),
            @Mapping(target = "lastEditor", ignore = true),
            @Mapping(target = "username",   ignore = true),
    }) // @formatter:on
    UserConfig formToUserConfig(UserConfigForm userConfigForm);

    @InheritConfiguration
    void updateFromForm(@MappingTarget UserConfig userConfig, UserConfigForm userConfigForm);

    ImportHolidayInfo toImportHolidayInfo(UserConfig userConfig);

}
