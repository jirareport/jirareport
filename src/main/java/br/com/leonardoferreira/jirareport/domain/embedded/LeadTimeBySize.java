package br.com.leonardoferreira.jirareport.domain.embedded;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 3/20/18 10:34 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadTimeBySize implements Serializable {
    private static final long serialVersionUID = 2418710552243783777L;

    private String size;

    private Double leadtime;

}
