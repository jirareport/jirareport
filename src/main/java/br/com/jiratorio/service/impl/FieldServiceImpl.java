package br.com.jiratorio.service.impl;

import br.com.jiratorio.client.FieldClient;
import br.com.jiratorio.domain.vo.JiraField;
import br.com.jiratorio.service.FieldService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FieldServiceImpl extends AbstractService implements FieldService {

    private final FieldClient fieldClient;

    public FieldServiceImpl(final FieldClient fieldClient) {
        this.fieldClient = fieldClient;
    }

    @Override
    @Cacheable("findAllFields")
    @Transactional(readOnly = true)
    public List<JiraField> findAllJiraFields() {
        log.info("Method=findAllJiraFields");
        return fieldClient.findAll(currentToken());
    }

}
