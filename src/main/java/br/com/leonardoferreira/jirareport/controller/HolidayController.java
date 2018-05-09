package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import java.util.List;
import org.springframework.http.HttpEntity;
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
 * @author s2it_leferreira
 * @since 5/7/18 6:53 PM
 */
@Controller
@RequestMapping("holidays")
public class HolidayController extends AbstractController {

    private final HolidayService holidayService;

    public HolidayController(final HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public ModelAndView index() {
        final List<Holiday> holidays = holidayService.findAll();
        return new ModelAndView("holidays/index")
                .addObject("holidays", holidays);
    }

    @GetMapping("/new")
    public ModelAndView create() {
        return new ModelAndView("holidays/new")
                .addObject("holiday", new Holiday());
    }

    @PostMapping
    public ModelAndView create(@Validated final Holiday holiday,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("holidays/new")
                    .addObject("holiday", holiday);
        }

        holidayService.create(holiday);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso");
        return new ModelAndView("redirect:/holidays");
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        holidayService.delete(id);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso");
        return new ModelAndView("redirect:/holidays");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView update(@PathVariable final Long id)  {
        Holiday holiday = holidayService.findById(id);
        return new ModelAndView("holidays/edit")
                .addObject("holiday", holiday);
    }

    @PutMapping
    public ModelAndView update(@Validated final Holiday holiday,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("holidays/edit")
                    .addObject("holiday", holiday);
        }

        holidayService.update(holiday);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso");
        return new ModelAndView("redirect:/holidays");
    }

}
