package br.com.leonardoferreira.jirareport.helper;

import br.com.leonardoferreira.jirareport.domain.DynamicFieldsValues;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class SelectHelper implements Helper {

    @Override
    public String getName() {
        return "selectHelper";
    }

    public boolean isSelected(final List<DynamicFieldsValues> list, final Integer index, final String item) {
        if (!CollectionUtils.isEmpty(list)) {
            DynamicFieldsValues dynamicFieldsValues = list.get(index);
            if (!StringUtils.isEmpty(dynamicFieldsValues.getField()) && !CollectionUtils.isEmpty(dynamicFieldsValues.getValues())) {
                return dynamicFieldsValues.getValues().contains(item);
            }
        }
        return false;
    }

}
