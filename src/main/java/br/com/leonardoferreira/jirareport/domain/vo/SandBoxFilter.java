package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.DynamicFieldsValues;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 5/12/18 3:47 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandBoxFilter {

    private List<String> keys;

    private List<String> estimatives;

    private List<String> systems;

    private List<String> epics;

    private List<String> issueTypes;

    private List<String> projects;

    private List<String> priorities;

    private List<DynamicFieldsValues> dynamicFieldsValues;

}
