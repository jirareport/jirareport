package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserConfigMapper {

    UserConfigForm userConfigToForm(UserConfig userConfig);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "lastEditor", ignore = true),
            @Mapping(target = "username", ignore = true),
    })
    UserConfig formToUserConfig(UserConfigForm userConfigForm);

    @InheritConfiguration
    void updateFromForm(@MappingTarget UserConfig userConfig, UserConfigForm userConfigForm);
}
