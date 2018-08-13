package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lferreira on 08/06/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraError {

    private String message;

    @JsonProperty("status-code")
    private Long statusCode;

    private List<String> errorMessages;

}
