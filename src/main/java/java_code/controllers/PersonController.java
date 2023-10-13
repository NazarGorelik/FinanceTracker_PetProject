package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java_code.dto.user.PersonDTO;
import java_code.dto.user.responses.AccountsResponse;
import java_code.security.UserPrincipal;
import java_code.services.PersonService;
import java_code.util.exceptions.ErrorUtil;
import java_code.util.exceptions.presentationLayer.InvalidCredentialsException;
import java_code.util.validators.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PersonController {

    private final PersonService personService;
    private final PersonValidator personValidator;
    private final ErrorUtil errorUtil;


    @GetMapping("/accounts")
    public AccountsResponse getAccounts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new AccountsResponse(personService.getAccountsById(userPrincipal.getPerson().getId()));
    }

    @PutMapping("/updatePerson")
    public String updatePerson(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody @Valid PersonDTO updatedUser, BindingResult bindingResult,
                                               @RequestParam("password") String password) {
        personValidator.validate(updatedUser, bindingResult);
        if (bindingResult.hasErrors())
            throw new InvalidCredentialsException(errorUtil.builtErrorResponse(bindingResult));

        personService.update(userPrincipal.getPerson().getId(), updatedUser, password);
        return "Please log in again!";
    }

    @DeleteMapping("/deletePerson")
    public String deletePerson(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestParam("password") String password) {
        personService.delete(userPrincipal.getPerson().getId(), password);
        return "Your account was deleted";
    }
}
