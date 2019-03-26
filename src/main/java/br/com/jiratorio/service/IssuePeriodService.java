package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest;
import br.com.jiratorio.domain.response.IssuePeriodResponse;
import java.util.List;

public interface IssuePeriodService {

    Long create(CreateIssuePeriodRequest createIssuePeriodRequest, Long boardId);

    IssuePeriod findById(Long issuePeriodId);

    void remove(Long issuePeriodId);

    void update(Long issuePeriodId);

    IssuePeriodResponse findIssuePeriodByBoard(Long boardId);

}
