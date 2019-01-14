package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.vo.JiraField;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "field-client", url = "${jira.url}")
public interface FieldClient {

    @GetMapping("/rest/api/2/field")
    List<JiraField> findAll(@RequestHeader("Authorization") String token);

}
