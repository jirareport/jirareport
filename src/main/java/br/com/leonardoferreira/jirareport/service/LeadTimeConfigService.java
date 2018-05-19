package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;

/**
 * @author lferreira on 17/05/18
 */
public interface LeadTimeConfigService {

    List<LeadTimeConfig> findAllByProjectId(Long projectId);

    void create(Long projectId, LeadTimeConfig leadTimeConfig);

    LeadTimeConfig findByProjectAndId(Long projectId, Long id);

    void update(Long projectId, LeadTimeConfig leadTimeConfig);

    void deleteByProjectAndId(Long projectId, Long id);
}
