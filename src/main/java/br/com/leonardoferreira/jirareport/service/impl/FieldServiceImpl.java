package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;

import br.com.leonardoferreira.jirareport.client.FieldClient;
import br.com.leonardoferreira.jirareport.domain.vo.JiraField;
import br.com.leonardoferreira.jirareport.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FieldServiceImpl extends AbstractService implements FieldService {

    @Autowired
    private FieldClient fieldClient;

    @Override
    @Transactional(readOnly = true)
    public List<JiraField> findAllJiraFields() {
        log.info("Method=findAllJiraFields");
        return fieldClient.findAll(currentToken());
    }

}
