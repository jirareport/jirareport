package br.com.jiratorio.mapper.util;

import br.com.jiratorio.util.DateUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class StringTransformer {

    @Named("toUpperCase")
    public String toUpperCase(final String str) {
        return str == null ? null : str.toUpperCase(DateUtil.LOCALE_BR);
    }

    @Named("listToUpperCase")
    public List<String> listToUpperCase(final List<String> strs) {
        return strs == null ? null : strs.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

}
