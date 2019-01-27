package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.service.BoardService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/statuses")
public class BoardStatusController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public Set<String> findByBoardId(@PathVariable final Long boardId) {
        return boardService.findStatusFromBoardInJira(boardId);
    }

}
