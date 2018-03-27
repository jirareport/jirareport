package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.ProjectClient;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leferreira
 * @since 7/28/17 11:40 AM
 */
@Slf4j
@Service
public class ProjectServiceImpl extends AbstractService implements ProjectService {

    private final ProjectClient projectClient;

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectClient projectClient, ProjectRepository projectRepository) {
        this.projectClient = projectClient;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAll() {
        log.info("Method=findAll");
        return projectRepository.findAll();
    }

    @Override
    public List<Project> findAllInJira() {
        log.info("Method=findAllInJira");
        return projectClient.findAll(currentToken());
    }

    @Override
    public void create(final Project project) {
        log.info("Method=create, project={}", project);
        projectRepository.save(project);
    }

    @Override
    public void delete(Long id) {
        log.info("Method=delete, id={}", id);
        projectRepository.deleteById(id);
    }

    @Override
    public Project findById(Long id) {
        log.info("Method=findById, id={}", id);
        return projectRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void update(Project project) {
        log.info("Method=project, project={}", project);
        projectRepository.save(project);
    }
}
