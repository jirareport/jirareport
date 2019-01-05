package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import br.com.leonardoferreira.jirareport.domain.vo.JiraField;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/boards")
public class BoardController extends AbstractController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FieldService fieldService;

    @GetMapping
    public ModelAndView index(final Pageable pageable, final Board board) {
        Page<Board> boards = boardService.findAll(pageable, board);
        List<String> owners = boardService.findAllOwners();

        return new ModelAndView("boards/index")
                .addObject("boards", boards)
                .addObject("board", board)
                .addObject("owners", owners);
    }

    @GetMapping("/new")
    public ModelAndView create() {
        List<JiraProject> projects = boardService.findAllJiraProject();

        return new ModelAndView("boards/new")
                .addObject("projects", projects);
    }

    @PostMapping
    public ModelAndView create(final Board board) {
        boardService.create(board);

        return new ModelAndView(String.format("redirect:/boards/%s/edit", board.getId()));
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable final Long id) {
        boardService.delete(id);

        return new ModelAndView("redirect:/boards");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView update(@PathVariable final Long id) {
        BoardForm boardForm = boardService.findToUpdate(id);
        Set<String> statusFromProjectInJira = boardService.findStatusFromBoardInJira(id);
        List<JiraField> jiraFields = fieldService.findAllJiraFields();

        return new ModelAndView("boards/edit")
                .addObject("boardForm", boardForm)
                .addObject("suggestedStatus", statusFromProjectInJira)
                .addObject("jiraFields", jiraFields);
    }

    @PutMapping
    public ModelAndView update(final BoardForm board, final RedirectAttributes redirectAttributes) {
        board.cleanDynamicFields();
        boardService.update(board);

        addFlashSuccess(redirectAttributes, "Alterações salvas com sucesso.");
        return new ModelAndView("redirect:/boards");
    }

}
