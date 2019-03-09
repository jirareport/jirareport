package br.com.jiratorio.client;

import br.com.jiratorio.domain.BoardStatusList;
import br.com.jiratorio.domain.JiraProject;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "project-client", url = "${jira.url}")
public interface ProjectClient {

    @GetMapping("/rest/api/2/project")
    List<JiraProject> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/rest/api/2/project/{projectId}/statuses")
    List<BoardStatusList> findStatusFromProject(@RequestHeader("Authorization") String token,
                                                @PathVariable("projectId") Long projectId);

    @GetMapping("/rest/api/2/project/{projectId}")
    JiraProject findById(@RequestHeader("Authorization") String token,
                         @PathVariable("projectId") Long projectId);
}
