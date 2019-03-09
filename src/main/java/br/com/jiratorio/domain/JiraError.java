package br.com.jiratorio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraError {

    private String message;

    @JsonProperty("status-code")
    private Long statusCode;

    private List<String> errorMessages;

}
