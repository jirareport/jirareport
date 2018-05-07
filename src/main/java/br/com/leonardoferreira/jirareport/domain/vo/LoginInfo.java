package br.com.leonardoferreira.jirareport.domain.vo;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author s2it_leferreira
 * @since 5/7/18 7:48 PM
 */
@Data
public class LoginInfo {

    private Long failedLoginCount;

    private Long loginCount;

    private String lastFailedLoginTime;

    private String previousLoginTime;
}
