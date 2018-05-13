package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Project;

import java.util.List;
import java.util.Set;

/**
 * Created by lferreira on 3/26/18
 */
public interface ProjectService {
    List<Project> findAll();

    List<Project> findAllInJira();

    void create(Project project);

    void delete(Long id);

    Project findById(Long id);

    void update(Project project);

    Set<String> findStatusFromProjectInJira(Project project);
}
