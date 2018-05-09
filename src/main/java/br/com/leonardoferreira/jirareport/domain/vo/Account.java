package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author lferreira
 * @since 7/28/17 11:02 AM
 */
@Data
public class Account {

    private String token;

    private CurrentUser currentUser;

}
