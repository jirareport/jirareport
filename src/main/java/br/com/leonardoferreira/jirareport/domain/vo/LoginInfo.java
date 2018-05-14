package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author lferreira
 * @since 5/7/18 7:48 PM
 */
@Data
public class LoginInfo {

    private Long failedLoginCount;

    private Long loginCount;

    private String lastFailedLoginTime;

    private String previousLoginTime;
}
