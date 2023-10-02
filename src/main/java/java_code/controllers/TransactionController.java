package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java_code.dto.user.TransactionDTO;
import java_code.dto.user.responses.TransactionsResponse;
import java_code.security.UserPrincipal;
import java_code.services.TransactionService;
import java_code.util.ErrorUtil;
import java_code.util.exceptions.presentationLayer.TransactionNotCreatedException;
import java_code.util.validators.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionValidator transactionValidator;
    private final ErrorUtil errorUtil;


    @PostMapping("/{accountName}/saveTransaction")
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid TransactionDTO transactionDTO, BindingResult bindingResult,
                                           @PathVariable("accountName") String accountName,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        transactionValidator.validate(transactionDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new TransactionNotCreatedException(errorUtil.builtErrorResponse(bindingResult));

        String username = userPrincipal.getUsername();
        transactionService.save(transactionDTO, username, accountName);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{accountName}/getTransactions")
    public TransactionsResponse getTransactions(@PathVariable("accountName") String accountName,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        return new TransactionsResponse(transactionService.getTransactionsByAccountName(accountName, username));
    }
}
