package br.com.leonardoferreira.jirareport.util;

import java.text.Normalizer;

public class StringUtil {

    public static String applyRulesForHolidaysService(String text){
        return removeAccents(text).toUpperCase().replace(" ", "_");
    }

    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
