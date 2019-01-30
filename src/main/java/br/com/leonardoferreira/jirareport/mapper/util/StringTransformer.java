package br.com.leonardoferreira.jirareport.mapper.util;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class StringTransformer {

    @Named("toUpperCase")
    public String toUpperCase(final String str) {
        return str == null ? null : str.toUpperCase(DateUtil.LOCALE_BR);
    }

}
