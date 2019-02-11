package br.com.jiratorio.controller;

import br.com.jiratorio.domain.form.UserConfigForm;
import br.com.jiratorio.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController  {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserConfigForm edit() {
        return userService.myInfo();
    }

    @PutMapping
    public ResponseEntity<Object> edit(@Valid @RequestBody final UserConfigForm userConfigForm) {
        userService.update(userConfigForm);

        return ResponseEntity.noContent().build();
    }

}
