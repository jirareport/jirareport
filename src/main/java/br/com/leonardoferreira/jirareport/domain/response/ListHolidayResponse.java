package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class ListHolidayResponse {

    private Page<Holiday> holidays;

    private Board board;

}
