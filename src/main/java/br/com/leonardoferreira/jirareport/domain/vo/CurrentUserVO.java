package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leferreira
 * @since 7/28/17 11:02 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserVO {

    private String name;

    private String email;

}
