package br.com.leonardoferreira.jirareport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lferreira on 3/26/18
 */
@FeignClient(name = "issue-client", url = "${jira.url}")
public interface IssueClient {

    @GetMapping("/rest/api/2/search?expand=changelog")
    String findAll(@RequestHeader("cookie") String token, @RequestParam("jql") String jql);

}
