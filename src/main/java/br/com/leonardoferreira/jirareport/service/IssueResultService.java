package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.IssueResult;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssueResultChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssueResultException;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
public interface IssueResultService {
    void create(IssueForm issueForm) throws CreateIssueResultException;

    List<IssueResult> findByProjectId(Long projectId);

    IssueResultChartVO getChartByIssues(List<IssueResult> issues);

    IssueResult findById(IssueForm issueForm);

    void remove(IssueForm issueForm);

    void update(IssueForm issueForm) throws CreateIssueResultException;
}
