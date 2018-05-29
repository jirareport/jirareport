package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 7/28/17 11:02 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String token;

    private CurrentUser currentUser;

}
