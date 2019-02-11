package br.com.jiratorio.controller;

import br.com.jiratorio.service.BoardService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/statuses")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BoardStatusController {

    private final BoardService boardService;

    @GetMapping
    public Set<String> findByBoardId(@PathVariable final Long boardId) {
        return boardService.findStatusFromBoardInJira(boardId);
    }

}
