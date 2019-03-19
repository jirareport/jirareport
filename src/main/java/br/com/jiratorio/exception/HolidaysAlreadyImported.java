package br.com.jiratorio.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Holidays already imported")
public class HolidaysAlreadyImported extends RuntimeException {
    private static final long serialVersionUID = 1189381143800680941L;

    public HolidaysAlreadyImported(final Exception e) {
        super(e);
    }

}