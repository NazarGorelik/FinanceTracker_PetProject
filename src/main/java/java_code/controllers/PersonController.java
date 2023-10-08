package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java_code.dto.user.responses.AccountsResponse;
import java_code.security.UserPrincipal;
import java_code.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PersonController {

    private final PersonService personService;


    @GetMapping("/accounts")
    public AccountsResponse getAccounts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new AccountsResponse(personService.getAccountsById(userPrincipal.getPerson().getId()));
    }
}
