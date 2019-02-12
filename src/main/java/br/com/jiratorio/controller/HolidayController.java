package br.com.jiratorio.controller;

import br.com.jiratorio.domain.request.HolidayRequest;
import br.com.jiratorio.domain.response.HolidayResponse;
import br.com.jiratorio.service.HolidayService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/boards/{boardId}/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(final HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public Page<HolidayResponse> index(@PathVariable final Long boardId,
                                       @PageableDefault(sort = "date") final Pageable pageable) {
        return holidayService.findByBoard(boardId, pageable);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable final Long boardId,
                                    @Valid @RequestBody final HolidayRequest holiday) {
        Long id = holidayService.create(boardId, holiday);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        holidayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<?> importFromAPI(@PathVariable final Long boardId) {
        holidayService.createImported(boardId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public HolidayResponse findById(@PathVariable final Long id) {
        return holidayService.findById(id);
    }

    @PutMapping("/{holidayId}")
    public ResponseEntity<?> update(@PathVariable final Long boardId,
                                    @PathVariable final Long holidayId,
                                    @Valid @RequestBody final HolidayRequest holidayRequest) {
        holidayService.update(boardId, holidayId, holidayRequest);
        return ResponseEntity.noContent().build();
    }

}
