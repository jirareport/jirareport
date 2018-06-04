package br.com.leonardoferreira.jirareport.controller;

import java.util.List;
import java.util.Set;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author lferreira on 17/05/18
 */
@Controller
@RequestMapping("/boards/{boardId}/lead-time-configs")
public class LeadTimeConfigController extends AbstractController {

    @Autowired
    private LeadTimeConfigService leadTimeConfigService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long boardId) {
        List<LeadTimeConfig> leadTimeConfigs = leadTimeConfigService.findAllByBoardId(boardId);

        return new ModelAndView("lead-time-configs/index")
                .addObject("boardId", boardId)
                .addObject("leadTimeConfigs", leadTimeConfigs);
    }

    @GetMapping("/new")
    public ModelAndView create(@PathVariable final Long boardId) {
        Board board = boardService.findById(boardId);
        Set<String> suggestedStatus = boardService.findStatusFromBoardInJira(board);

        return new ModelAndView("lead-time-configs/new")
                .addObject("leadTimeConfig", new LeadTimeConfig())
                .addObject("suggestedStatus", suggestedStatus);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long boardId,
                               @Validated final LeadTimeConfig leadTimeConfig,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Set<String> suggestedStatus = boardService.findStatusFromBoardInJira(boardId);

            return new ModelAndView("lead-time-configs/new")
                    .addObject("leadTimeConfig", leadTimeConfig)
                    .addObject("suggestedStatus", suggestedStatus);
        }

        leadTimeConfigService.create(boardId, leadTimeConfig);

        addFlashSuccess(redirectAttributes, "Registro inserido com sucesso.");
        return new ModelAndView(String.format("redirect:/boards/%s/lead-time-configs", boardId));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView update(@PathVariable final Long boardId,
                               @PathVariable final Long id) {
        LeadTimeConfig leadTimeConfig = leadTimeConfigService.findByBoardAndId(boardId, id);
        Set<String> suggestedStatus = boardService.findStatusFromBoardInJira(boardId);

        return new ModelAndView("lead-time-configs/edit")
                .addObject("leadTimeConfig", leadTimeConfig)
                .addObject("suggestedStatus", suggestedStatus);
    }

    @PutMapping
    public ModelAndView update(@PathVariable final Long boardId,
                               @Validated final LeadTimeConfig leadTimeConfig,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Set<String> suggestedStatus = boardService.findStatusFromBoardInJira(boardId);

            return new ModelAndView("lead-time-configs/new")
                    .addObject("leadTimeConfig", leadTimeConfig)
                    .addObject("suggestedStatus", suggestedStatus);
        }

        leadTimeConfigService.update(boardId, leadTimeConfig);

        addFlashSuccess(redirectAttributes, "Registro atualizado com sucesso.");
        return new ModelAndView(String.format("redirect:/boards/%s/lead-time-configs", boardId));
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable final Long boardId,
                               @PathVariable final Long id,
                               final RedirectAttributes redirectAttributes) {
        leadTimeConfigService.deleteByBoardAndId(boardId, id);

        addFlashSuccess(redirectAttributes, "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/boards/%s/lead-time-configs", boardId));
    }

}
