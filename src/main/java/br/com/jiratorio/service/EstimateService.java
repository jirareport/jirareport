package br.com.jiratorio.service;

import br.com.jiratorio.domain.form.EstimateForm;
import br.com.jiratorio.domain.vo.EstimateIssue;

import java.util.List;

public interface EstimateService {

    List<EstimateIssue> findEstimateIssues(Long boardId, EstimateForm estimateForm);
}
