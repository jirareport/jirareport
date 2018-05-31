package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;

/**
 * @author lferreira on 17/05/18
 */
public interface LeadTimeConfigService {

    List<LeadTimeConfig> findAllByBoardId(Long boardId);

    void create(Long boardId, LeadTimeConfig leadTimeConfig);

    LeadTimeConfig findByBoardAndId(Long boardId, Long id);

    void update(Long boardId, LeadTimeConfig leadTimeConfig);

    void deleteByBoardAndId(Long boardId, Long id);
}
