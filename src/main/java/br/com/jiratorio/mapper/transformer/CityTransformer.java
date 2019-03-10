package br.com.jiratorio.mapper.transformer;

import br.com.jiratorio.util.DateUtil;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class CityTransformer {

    private final StringTransformer stringTransformer;

    public CityTransformer(final StringTransformer stringTransformer) {
        this.stringTransformer = stringTransformer;
    }

    @Named("normalizeCity")
    public String normalizeCity(final String city) {
        if (city == null) {
            return null;
        }

        return stringTransformer.stripAccents(city)
                .replaceAll(" ", "_")
                .toUpperCase(DateUtil.LOCALE_BR);
    }

}
