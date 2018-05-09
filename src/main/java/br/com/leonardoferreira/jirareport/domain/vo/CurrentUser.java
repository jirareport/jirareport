package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 7/28/17 11:02 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {

    @JsonProperty("displayName")
    private String name;

    @JsonProperty("emailAddress")
    private String email;

}
