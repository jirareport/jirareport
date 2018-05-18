package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.repository.LeadTimeConfigRepository;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira on 17/05/18
 */
@Service
public class LeadTimeConfigServiceImpl implements LeadTimeConfigService {

    @Autowired
    private LeadTimeConfigRepository leadTimeConfigRepository;

    @Autowired
    private ProjectService projectService;

    @Override
    @Transactional(readOnly = true)
    public List<LeadTimeConfig> findAllByProjectId(final Long projectId) {
        return leadTimeConfigRepository.findByProjectId(projectId);
    }

    @Override
    @Transactional
    public void create(final Long projectId, final LeadTimeConfig leadTimeConfig) {
        Project project = projectService.findById(projectId);
        leadTimeConfig.setProject(project);

        leadTimeConfigRepository.save(leadTimeConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadTimeConfig findByProjectAndId(final Long projectId, final Long id) {
        return leadTimeConfigRepository.findByIdAndProjectId(id, projectId);
    }

    @Override
    @Transactional
    public void update(final Long projectId, final LeadTimeConfig leadTimeConfig) {
        Project project = projectService.findById(projectId);
        leadTimeConfig.setProject(project);

        leadTimeConfigRepository.save(leadTimeConfig);
    }

    @Override
    @Transactional
    public void deleteByProjectAndId(final Long projectId, final Long id) {
        leadTimeConfigRepository.deleteByIdAndProjectId(id, projectId);
    }
}
