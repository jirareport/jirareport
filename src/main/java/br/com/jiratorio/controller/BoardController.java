package br.com.jiratorio.controller;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.request.CreateBoardRequest;
import br.com.jiratorio.domain.request.UpdateBoardRequest;
import br.com.jiratorio.domain.response.BoardDetailsResponse;
import br.com.jiratorio.domain.response.BoardResponse;
import br.com.jiratorio.service.BoardService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public Page<BoardResponse> index(final Pageable pageable, final Board board) {
        return boardService.findAll(pageable, board);
    }

    @GetMapping("/owners")
    public List<String> owners() {
        return boardService.findAllOwners();
    }

    @GetMapping("/{id}")
    public BoardDetailsResponse findById(@PathVariable final Long id) {
        return boardService.findDetailsById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody final CreateBoardRequest board) {
        Long boardId = boardService.create(board);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(boardId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final Long id,
                                    @RequestBody final UpdateBoardRequest updateBoardRequest) {
        boardService.update(id, updateBoardRequest);
        return ResponseEntity.noContent().build();
    }

}
