package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUserVO;
import br.com.leonardoferreira.jirareport.domain.vo.SessionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Created by lferreira on 3/26/18
 */
@FeignClient(name = "auth-client", url = "${jira.url}")
public interface AuthClient {

    @GetMapping("/rest/auth/1/session")
    SessionInfo login(@RequestBody AccountForm accountForm);

    @GetMapping("/rest/api/2/myself")
    CurrentUserVO findCurrentUser(@RequestHeader("cookie") String token);

}
