package br.com.jiratorio.config;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class FormatterImpl implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(final String text, final Locale locale) throws ParseException {
        return LocalDate.parse(text, defaultFormatter());
    }

    @Override
    public String print(final LocalDate object, final Locale locale) {
        return defaultFormatter().format(object);
    }

    private DateTimeFormatter defaultFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

}
