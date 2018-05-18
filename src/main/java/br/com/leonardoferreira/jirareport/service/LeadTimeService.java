package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;

/**
 * @author lferreira on 17/05/18
 */
public interface LeadTimeService {

    void createLeadTimes(List<Issue> issues, Long projectId);

}
