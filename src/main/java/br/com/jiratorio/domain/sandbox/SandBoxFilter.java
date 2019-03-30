package br.com.jiratorio.domain.sandbox;

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandBoxFilter {

    private Set<String> keys;

    private Set<String> estimatives;

    private Set<String> systems;

    private Set<String> epics;

    private Set<String> issueTypes;

    private Set<String> projects;

    private Set<String> priorities;

    private List<DynamicFieldsValues> dynamicFieldsValues;

}
