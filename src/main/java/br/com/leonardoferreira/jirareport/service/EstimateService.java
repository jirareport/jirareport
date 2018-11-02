package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;

import java.util.List;

public interface EstimateService {

    List<EstimateIssue> findEstimateIssues(Long boardId, EstimateForm estimateForm);
}
