package br.com.jiratorio.client;

import br.com.jiratorio.domain.vo.CurrentUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-client", url = "${jira.url}")
public interface AuthClient {

    @GetMapping("/rest/api/2/myself")
    CurrentUser findCurrentUser(@RequestHeader("Authorization") String token);

}
