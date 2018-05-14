package br.com.leonardoferreira.jirareport.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.client.ProjectClient;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.vo.Statuses;
import br.com.leonardoferreira.jirareport.domain.vo.StatusesProject;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lferreira
 * @since 7/28/17 11:40 AM
 */
@Slf4j
@Service
public class ProjectServiceImpl extends AbstractService implements ProjectService {

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> findAll() {
        log.info("Method=findAll");
        return (List<Project>) projectRepository.findAll();
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
    public void delete(final Long id) {
        log.info("Method=delete, id={}", id);
        projectRepository.deleteById(id);
    }

    @Override
    public Project findById(final Long id) {
        log.info("Method=findById, id={}", id);
        return projectRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void update(final Project project) {
        log.info("Method=project, project={}", project);
        projectRepository.save(project);
    }

    @Override
    public Set<String> findStatusFromProjectInJira(final Project project) {
        List<StatusesProject> listStatusesProject = projectClient.findStatusFromProject(currentToken(), project.getId());

        return listStatusesProject.stream()
                .map(StatusesProject::getStatuses)
                .flatMap(Collection::stream)
                .map(Statuses::getName)
                .collect(Collectors.toSet());
    }
}
