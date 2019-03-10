package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.Board;
import java.time.LocalDate;

public interface JQLService {

    String finalizedIssues(Board board, LocalDate start, LocalDate end);

    String openedIssues(Board board);

}
