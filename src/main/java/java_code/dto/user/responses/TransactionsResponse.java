package java_code.dto.user.responses;

import java_code.dto.user.TransactionDTO;

import java.util.List;

public record TransactionsResponse (List<TransactionDTO> transactions){}
