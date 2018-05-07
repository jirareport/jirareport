package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Project;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Created by lferreira on 3/26/18
 */
@FeignClient(name = "project-client", url = "${jira.url}")
public interface ProjectClient {

    @GetMapping("/rest/api/2/project")
    List<Project> findAll(@RequestHeader("cookie") String token);

}
