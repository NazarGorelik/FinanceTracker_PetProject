package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java_code.dto.user.AccountDTO;
import java_code.security.UserPrincipal;
import java_code.services.AccountService;
import java_code.util.ErrorUtil;
import java_code.util.exceptions.presentationLayer.AccountNotCreatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;
    private final ErrorUtil errorUtil;


    @PostMapping("/saveAccount")
    public ResponseEntity<HttpStatus> createAccount(@RequestBody @Valid AccountDTO accountDTO, BindingResult bindingResult,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (bindingResult.hasErrors())
            throw new AccountNotCreatedException(errorUtil.builtErrorResponse(bindingResult));

        String username = userPrincipal.getUsername();
        accountService.save(accountDTO, username);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
