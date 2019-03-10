package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.FluxColumn;
import br.com.jiratorio.domain.JQLBuilder;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.service.JQLService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JQLServiceImpl implements JQLService {

    @Override
    public String finalizedIssues(final Board board, final LocalDate start, final LocalDate end) {
        log.info("Method=finalizedIssues, board={}, startDate={}, endDate={}", board, start, end);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = start.format(formatter);
        String endDate = end.format(formatter) + " 23:59";
        FluxColumn fluxColumn = new FluxColumn(board);

        return new JQLBuilder()
                .append(" project = {project} ", board.getExternalId())
                .append(" AND ( STATUS CHANGED TO {endColumn} DURING({startDate}, {endDate}) ", board.getEndColumn(), startDate, endDate)
                .append("       OR ( STATUS CHANGED TO {lastColumn} DURING ({startDate}, {endDate}) AND NOT STATUS CHANGED TO {endColumn} )",
                        fluxColumn.getLastColumn(), startDate, endDate, board.getEndColumn())
                .append("       OR ( resolutiondate >= {startDate} AND resolutiondate <= {endDate} ", startDate, endDate)
                .append("              AND NOT STATUS CHANGED TO {lastColumn} AND NOT STATUS CHANGED TO {endColumn})",
                        fluxColumn.getLastColumn(), board.getEndColumn())
                .append("     ) ")
                .append(" AND issueType NOT IN ({issueTypes}) ", board.getIgnoreIssueType())
                .append(" AND status WAS IN ({startColumns}) ", fluxColumn.getStartColumns())
                .append(" AND status IN ({endColumns}) ", fluxColumn.getEndColumns())
                .build();
    }

    @Override
    public String openedIssues(final Board board) {
        log.info("Method=openedIssues, board={}", board);

        FluxColumn fluxColumn = new FluxColumn(board);

        return new JQLBuilder()
                .append(" project = {project} ", board.getExternalId())
                .append(" AND issueType NOT IN ({issueTypes}) ", board.getIgnoreIssueType())
                .append(" AND status IN ({fluxColumns}) ", fluxColumn.getWipColumns())
                .build();
    }

}
