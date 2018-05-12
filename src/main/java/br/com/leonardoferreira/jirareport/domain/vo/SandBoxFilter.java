package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;
import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 5/12/18 3:47 PM
 */
@Data
public class SandBoxFilter {

    private List<String> keys;

    private List<String> estimatives;

    private List<String> systems;

    private List<String> epics;

}
