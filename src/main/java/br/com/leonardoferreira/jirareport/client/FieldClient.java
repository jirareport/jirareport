package br.com.leonardoferreira.jirareport.client;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.vo.JiraField;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author lferreira on 07/06/18
 */
@FeignClient(name = "field-client", url = "${jira.url}")
public interface FieldClient {

    @Cacheable("findAllFields")
    @GetMapping("/rest/api/2/field")
    List<JiraField> findAll(@RequestHeader("Authorization") String token);

}
