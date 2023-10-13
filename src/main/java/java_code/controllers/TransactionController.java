package java_code.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java_code.dto.user.TransactionDTO;
import java_code.dto.user.responses.TransactionsResponse;
import java_code.security.UserPrincipal;
import java_code.services.TransactionService;
import java_code.util.exceptions.ErrorUtil;
import java_code.util.exceptions.presentationLayer.TransactionNotCreatedException;
import java_code.util.validators.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @PostMapping("/{accountName}/save")
    public HttpStatus save(@RequestBody @Valid TransactionDTO transactionDTO, BindingResult bindingResult,
                                           @PathVariable("accountName") String accountName,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        transactionValidator.validate(transactionDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new TransactionNotCreatedException(errorUtil.builtErrorResponse(bindingResult));

        int userID = userPrincipal.getPerson().getId();
        transactionService.save(transactionDTO, userID, accountName);
        return HttpStatus.OK;
    }

    @GetMapping("/{accountName}/transactions")
    public TransactionsResponse getTransactions(@PathVariable("accountName") String accountName,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userID = userPrincipal.getPerson().getId();
        return new TransactionsResponse(transactionService.getTransactionsByAccountName(accountName, userID));
    }
}
