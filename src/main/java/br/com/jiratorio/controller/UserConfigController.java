package br.com.jiratorio.controller;

import br.com.jiratorio.domain.Account;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.jiratorio.domain.response.UserConfigResponse;
import br.com.jiratorio.service.UserConfigService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/me/configs")
public class UserConfigController {

    private final UserConfigService userConfigService;

    public UserConfigController(final UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @GetMapping
    public UserConfigResponse findMyConfig(@AuthenticationPrincipal final Account account) {
        return userConfigService.findByUsername(account.getUsername());
    }

    @PutMapping
    public ResponseEntity<?> updateMyConfig(@Valid @RequestBody final UpdateUserConfigRequest updateUserConfigRequest,
                                            @AuthenticationPrincipal final Account account) {
        userConfigService.update(account.getUsername(), updateUserConfigRequest);
        return ResponseEntity.noContent().build();
    }

}
