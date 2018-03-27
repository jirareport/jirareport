package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Project;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
public interface ProjectClient {
    List<Project> findAll(String token);
}
