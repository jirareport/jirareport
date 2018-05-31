package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.vo.BoardStatusList;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
@FeignClient(name = "project-client", url = "${jira.url}")
public interface ProjectClient {

    @GetMapping("/rest/api/2/project")
    List<JiraProject> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/rest/api/2/project/{projectId}/statuses")
    List<BoardStatusList> findStatusFromProject(@RequestHeader("Authorization") String token,
                                                @PathVariable("projectId") Long projectId);

}
