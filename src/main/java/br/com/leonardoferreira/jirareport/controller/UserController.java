package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;
import br.com.leonardoferreira.jirareport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @GetMapping("/me/edit")
    public ModelAndView edit() {
        UserConfigForm userConfigForm = userService.myInfo();
        return new ModelAndView("users/edit")
                .addObject("userConfigForm", userConfigForm);
    }

    @PutMapping
    public ModelAndView edit(@Valid final UserConfigForm userConfigForm,
                             final BindingResult bindingResult,
                             final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("users/edit")
                    .addObject("userConfigForm", userConfigForm);
        }
        userService.update(userConfigForm);
        addFlashSuccess(redirectAttributes, "Registro atualizado com sucesso.");
        return new ModelAndView("redirect:/users/me/edit");
    }

}
