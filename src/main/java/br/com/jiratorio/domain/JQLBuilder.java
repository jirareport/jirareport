package br.com.jiratorio.domain;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class JQLBuilder {

    private final StringBuilder jql;

    public JQLBuilder() {
        this.jql = new StringBuilder();
    }

    public JQLBuilder append(final String jql, final Object... params) {
        if (hasNullParams(params)) {
            return this;
        }

        String result = jql;

        Matcher matcher = Pattern.compile("\\{\\w+}").matcher(jql);
        for (int i = 0; matcher.find() && i < params.length; i++) {
            result = result.replaceAll(Pattern.quote(matcher.group()), wrap(params[i]));
        }

        this.jql.append(result);

        return this;
    }

    public String build() {
        return jql.toString();
    }

    private String wrap(final Object param) {
        return param instanceof Collection ? wrapList((Collection<?>) param) : "'" + param + "'";
    }

    private String wrapList(final Collection<?> list) {
        return list.stream().map(this::wrap).collect(Collectors.joining(","));
    }

    private boolean hasNullParams(final Object... params) {
        for (Object param : params) {
            if (isNull(param)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNull(final Object param) {
        if (param instanceof String) {
            return StringUtils.isEmpty(param);
        }

        if (param instanceof Collection) {
            return CollectionUtils.isEmpty((Collection<?>) param);
        }

        return !Objects.nonNull(param);
    }

}
