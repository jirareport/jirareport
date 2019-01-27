package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.response.ListLeadTimeConfigResponse;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/boards/{boardId}/lead-time-configs")
public class LeadTimeConfigController {

    @Autowired
    private LeadTimeConfigService leadTimeConfigService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ListLeadTimeConfigResponse index(@PathVariable final Long boardId) {
        List<LeadTimeConfig> leadTimeConfigs = leadTimeConfigService.findAllByBoardId(boardId);
        Board board = boardService.findById(boardId);

        return ListLeadTimeConfigResponse.builder()
                .leadTimeConfigs(leadTimeConfigs)
                .board(board)
                .build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@PathVariable final Long boardId,
                                         @Validated @RequestBody final LeadTimeConfig leadTimeConfig) {
        Long id = leadTimeConfigService.create(boardId, leadTimeConfig);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public LeadTimeConfig findById(@PathVariable final Long boardId,
                                   @PathVariable final Long id) {
        return leadTimeConfigService.findByBoardAndId(boardId, id);
    }

    @PutMapping
    public ResponseEntity<Object> update(@PathVariable final Long boardId,
                                         @Validated @RequestBody final LeadTimeConfig leadTimeConfig) {
        leadTimeConfigService.update(boardId, leadTimeConfig);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable final Long boardId,
                                         @PathVariable final Long id) {
        leadTimeConfigService.deleteByBoardAndId(boardId, id);
        return ResponseEntity.noContent().build();
    }
}
