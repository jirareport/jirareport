package br.com.jiratorio.service;

import java.util.List;

import br.com.jiratorio.domain.entity.Issue;

public interface LeadTimeService {

    void createLeadTimes(List<Issue> issues, Long boardId);

}
