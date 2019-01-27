package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.response.ListHolidayResponse;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/boards/{boardId}/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ListHolidayResponse index(@PathVariable final Long boardId,
                                     @PageableDefault(sort = "date") final Pageable pageable) {
        Page<Holiday> holidays = holidayService.findByBoard(boardId, pageable);
        Board board = boardService.findById(boardId);

        return new ListHolidayResponse(holidays, board);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable final Long boardId, @Valid final Holiday holiday) {
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
    public Holiday findById(@PathVariable final Long id) {
        return holidayService.findById(id);
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable final Long boardId,
                                    @Valid final Holiday holiday) {
        holidayService.update(boardId, holiday);
        return ResponseEntity.noContent().build();
    }

}
