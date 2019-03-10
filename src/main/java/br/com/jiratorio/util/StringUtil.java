package br.com.jiratorio.util;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

    public static String replaceParams(final String jql, final Map<String, Object> params) {
        String result = jql;
        for (Map.Entry<String, Object> obj : params.entrySet()) {
            String value = obj.getValue() instanceof Collection ? wrapList((Collection<?>) obj.getValue()) : wrap(obj.getValue());
            result = result.replaceAll("\\{" + obj.getKey() + "}", value);
        }

        return result;
    }

    public static String wrap(final Object obj) {
        return "'" + obj + "'";
    }

    public static String wrapList(final Collection<?> list) {
        return list.stream().map(StringUtil::wrap).collect(Collectors.joining(","));
    }

}
