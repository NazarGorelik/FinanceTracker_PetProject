package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java_code.dto.user.AccountDTO;
import java_code.security.UserPrincipal;
import java_code.services.AccountService;
import java_code.util.exceptions.ErrorUtil;
import java_code.util.exceptions.presentationLayer.AccountNotCreatedException;
import java_code.util.exceptions.presentationLayer.AccountNotUpdatedException;
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


    @PostMapping("/save")
    public ResponseEntity<HttpStatus> createAccount(@RequestBody @Valid AccountDTO accountDTO, BindingResult bindingResult,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (bindingResult.hasErrors())
            throw new AccountNotCreatedException(errorUtil.builtErrorResponse(bindingResult));

        int userID = userPrincipal.getPerson().getId();
        accountService.save(accountDTO, userID);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/update/{accountName}")
    public ResponseEntity<String> updateAccount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @RequestBody @Valid AccountDTO updatedAccount, BindingResult bindingResult,
                                                @PathVariable("accountName") String accountName) {
        if (bindingResult.hasErrors())
            throw new AccountNotUpdatedException("Incorrect data for new account!");

        accountService.update(userPrincipal.getPerson().getId(), updatedAccount, accountName);
        return new ResponseEntity<>("Account was changed!", HttpStatus.OK);
    }

    @DeleteMapping("delete/{accountName}")
    public void deleteAccount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                              @PathVariable("accountName") String accountName){
        accountService.delete(userPrincipal.getPerson().getId(), accountName);
    }
}
