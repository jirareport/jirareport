package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author leferreira
 * @since 7/28/17 11:02 AM
 */
@Data
public class AccountVO {

    private String token;

    private CurrentUserVO currentUser;

}
