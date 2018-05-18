package br.com.leonardoferreira.jirareport.controller;

import java.util.List;
import java.util.Set;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
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
@RequestMapping("/projects/{projectId}/lead-time-configs")
public class LeadTimeConfigController {

    @Autowired
    private LeadTimeConfigService leadTimeConfigService;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId) {
        List<LeadTimeConfig> leadTimeConfigs = leadTimeConfigService.findAllByProjectId(projectId);
        return new ModelAndView("lead-time-configs/index")
                .addObject("projectId", projectId)
                .addObject("leadTimeConfigs", leadTimeConfigs);
    }

    @GetMapping("/new")
    public ModelAndView create(@PathVariable final Long projectId) {
            Set<String> suggestedStatus = projectService.findStatusFromProjectInJira(projectId);

        return new ModelAndView("lead-time-configs/new")
                .addObject("leadTimeConfig", new LeadTimeConfig())
                .addObject("suggestedStatus", suggestedStatus);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long projectId,
                               @Validated final LeadTimeConfig leadTimeConfig,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Set<String> suggestedStatus = projectService.findStatusFromProjectInJira(projectId);

            return new ModelAndView("lead-time-configs/new")
                    .addObject("leadTimeConfig", leadTimeConfig)
                    .addObject("suggestedStatus", suggestedStatus);
        }

        leadTimeConfigService.create(projectId, leadTimeConfig);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%s/lead-time-configs", projectId));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView update(@PathVariable final Long projectId,
                               @PathVariable final Long id) {
        LeadTimeConfig leadTimeConfig = leadTimeConfigService.findByProjectAndId(projectId, id);
        Set<String> suggestedStatus = projectService.findStatusFromProjectInJira(projectId);

        return new ModelAndView("lead-time-configs/edit")
                .addObject("leadTimeConfig", leadTimeConfig)
                .addObject("suggestedStatus", suggestedStatus);
    }

    @PutMapping
    public ModelAndView update(@PathVariable final Long projectId,
                               @Validated final LeadTimeConfig leadTimeConfig,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Set<String> suggestedStatus = projectService.findStatusFromProjectInJira(projectId);

            return new ModelAndView("lead-time-configs/new")
                    .addObject("leadTimeConfig", leadTimeConfig)
                    .addObject("suggestedStatus", suggestedStatus);
        }

        leadTimeConfigService.update(projectId, leadTimeConfig);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%s/lead-time-configs", projectId));
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable final Long projectId,
                               @PathVariable final Long id,
                               final RedirectAttributes redirectAttributes) {
        leadTimeConfigService.deleteByProjectAndId(projectId, id);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%s/lead-time-configs", projectId));
    }

}
