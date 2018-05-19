package br.com.leonardoferreira.jirareport.util;

import java.text.Normalizer;

public final class StringUtil {

    private StringUtil() {
    }

    public static String applyRulesForHolidaysService(final String text) {
        return removeAccents(text).toUpperCase(DateUtil.LOCALE_BR).replace(" ", "_");
    }

    public static String removeAccents(final String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
