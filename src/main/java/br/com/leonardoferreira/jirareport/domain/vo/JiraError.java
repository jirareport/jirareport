package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author lferreira on 08/06/18
 */
@Data
public class JiraError {

    private String message;

    @SerializedName("status-code")
    private Long statusCode;

    private List<String> errorMessages;

}
