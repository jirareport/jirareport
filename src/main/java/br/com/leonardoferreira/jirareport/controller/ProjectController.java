package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author leferreira
 * @since 11/14/17 3:48 PM
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("projects/index")
                .addObject("projects", projectService.findAll());
    }

    @GetMapping("/new")
    public ModelAndView create() {
        return new ModelAndView("projects/new")
                .addObject("projects", projectService.findAllInJira());
    }

    @PostMapping
    public ModelAndView create(Project project) {
        projectService.create(project);
        return new ModelAndView(String.format("redirect:/projects/%s/edit", project.getId()));
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        projectService.delete(id);
        return new ModelAndView("redirect:/projects");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView update(@PathVariable Long id) {
        Project project = projectService.findById(id);
        return new ModelAndView("projects/edit")
                .addObject("project", project);
    }

    @PutMapping
    public ModelAndView update(Project project) {
        projectService.update(project);
        return new ModelAndView(String.format("redirect:/projects/%s/issues", project.getId()));
    }
}
