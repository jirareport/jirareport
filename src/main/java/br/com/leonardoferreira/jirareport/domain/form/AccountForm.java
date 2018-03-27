package br.com.leonardoferreira.jirareport.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 7/28/17 10:34 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {

    private String username;

    private String password;

}
