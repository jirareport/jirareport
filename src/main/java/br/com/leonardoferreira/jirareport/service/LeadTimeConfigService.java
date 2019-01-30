package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.request.LeadTimeConfigRequest;
import br.com.leonardoferreira.jirareport.domain.response.LeadTimeConfigResponse;
import java.util.List;

public interface LeadTimeConfigService {

    List<LeadTimeConfig> findAllByBoardId(Long boardId);

    List<LeadTimeConfigResponse> findAll(Long boardId);

    Long create(Long boardId, LeadTimeConfigRequest leadTimeConfigRequest);

    LeadTimeConfigResponse findByBoardAndId(Long boardId, Long id);

    void update(Long boardId, Long id, LeadTimeConfigRequest leadTimeConfigRequest);

    void deleteByBoardAndId(Long boardId, Long id);
}
