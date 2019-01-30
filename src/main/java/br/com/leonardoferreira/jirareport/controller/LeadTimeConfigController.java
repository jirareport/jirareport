package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.request.LeadTimeConfigRequest;
import br.com.leonardoferreira.jirareport.domain.response.LeadTimeConfigResponse;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<LeadTimeConfigResponse> index(@PathVariable final Long boardId) {
        return leadTimeConfigService.findAll(boardId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@PathVariable final Long boardId,
                                         @Valid @RequestBody final LeadTimeConfigRequest leadTimeConfigRequest) {
        Long id = leadTimeConfigService.create(boardId, leadTimeConfigRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public LeadTimeConfigResponse findById(@PathVariable final Long boardId,
                                   @PathVariable final Long id) {
        return leadTimeConfigService.findByBoardAndId(boardId, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final Long boardId,
                                         @PathVariable final Long id,
                                         @Valid @RequestBody final LeadTimeConfigRequest leadTimeConfigRequest) {
        leadTimeConfigService.update(boardId, id, leadTimeConfigRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long boardId,
                                         @PathVariable final Long id) {
        leadTimeConfigService.deleteByBoardAndId(boardId, id);
        return ResponseEntity.noContent().build();
    }
}
