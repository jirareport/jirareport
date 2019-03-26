package br.com.jiratorio.repository;

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.form.IssueForm;
import java.util.List;

public interface IssueCustomRepository {

    List<Issue> findByExample(Long boardId, IssueForm issueForm);

    List<DynamicFieldsValues> findAllDynamicFieldValues(Long boardId);

}
