package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.client.ProjectClient;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leferreira
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 7/28/17 11:40 AM
 */
@Service
public class ProjectService extends AbstractService {

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> findAllInJira() {
        return projectClient.findAll(currentToken());
    }

    public void create(final Project project) {
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    public void update(Project project) {
        projectRepository.save(project);
    }
}
