package br.com.leonardoferreira.jirareport.util;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s2it_leferreira
 * @since 13/06/18 10:00
 */
public final class StringUtil {

    private StringUtil() {
    }

    public static String replaceParams(final String jql, final Map<String, Object> params) {
        String result = jql;
        for (Map.Entry<String, Object> obj : params.entrySet()) {
            String value = obj.getValue() instanceof Collection ? wrapList((Collection<?>) obj.getValue()) : wrap(obj.getValue());
            result = result.replaceAll("\\{" + obj.getKey() + "\\}", value);
        }

        return result;
    }

    public static String wrap(final Object obj) {
        return "'" + obj + "'";
    }

    public static String wrapList(final Collection<?> list) {
        return String.join(",", list.stream().map(StringUtil::wrap).collect(Collectors.toList()));
    }
}
