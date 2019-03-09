package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.LeadTimeConfig;
import br.com.jiratorio.domain.request.LeadTimeConfigRequest;
import br.com.jiratorio.domain.response.LeadTimeConfigResponse;
import java.util.List;

public interface LeadTimeConfigService {

    List<LeadTimeConfig> findAllByBoardId(Long boardId);

    List<LeadTimeConfigResponse> findAll(Long boardId);

    Long create(Long boardId, LeadTimeConfigRequest leadTimeConfigRequest);

    LeadTimeConfigResponse findByBoardAndId(Long boardId, Long id);

    void update(Long boardId, Long id, LeadTimeConfigRequest leadTimeConfigRequest);

    void deleteByBoardAndId(Long boardId, Long id);
}
