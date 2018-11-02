package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.exception.InternalServerErrorException;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import br.com.leonardoferreira.jirareport.service.EstimateService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import br.com.leonardoferreira.jirareport.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class EstimateServiceImpl implements EstimateService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private IssueService issueService;

    @Override
    public List<EstimateIssue> findEstimateIssues(Long boardId, EstimateForm estimateForm) {
        final Board board = boardRepository.findById(boardId).orElseThrow(ResourceNotFound::new);

        List<EstimateIssue> issueList = issueService.findByJql(searchJQL(board), board);

        // Calçular o que precisar

        return null;
    }

    private String searchJQL(final Board board) {
        log.info("Method=searchJQL, board={}", board);


        // Precisa tirar a última coluna
        Set<String> fluxColumns = CalcUtil.calcWipColumns(board);
        if (fluxColumns == null || fluxColumns.isEmpty()){
            throw new InternalServerErrorException("O fluxo de colunas não está configurado");
        }
        Map<String, Object> params = new HashMap<>();

        StringBuilder jql = new StringBuilder();
        jql.append(" project = {project} ");

        if (board.getIgnoreIssueType() != null && !board.getIgnoreIssueType().isEmpty()) {
            jql.append(" AND issueType NOT IN ({issueTypes}) ");
            params.put("issueTypes", board.getIgnoreIssueType());
        }

        jql.append(" AND status IN ({fluxColumns}) ");

        params.put("project", board.getExternalId().toString());
        params.put("fluxColumns", fluxColumns);

        return StringUtil.replaceParams(jql.toString(), params);
    }
}
