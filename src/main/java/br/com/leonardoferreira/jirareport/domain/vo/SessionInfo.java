package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 5/7/18 7:47 PM
 */
@Data
public class SessionInfo {

    private Session session;

    private LoginInfo loginInfo;
}
