package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;

public interface LeadTimeService {

    void createLeadTimes(List<Issue> issues, Long boardId);

}
