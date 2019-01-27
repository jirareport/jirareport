package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListLeadTimeConfigResponse {

    private List<LeadTimeConfig> leadTimeConfigs;

    private Board board;

}
