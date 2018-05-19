package br.com.leonardoferreira.jirareport.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lferreira
 * @since 7/28/17 10:34 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "password" })
public class LoginForm {

    private String username;

    private String password;

}
