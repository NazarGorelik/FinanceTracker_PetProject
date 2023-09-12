package java_code.dto.admin;

import java.time.LocalDate;

public record AdminTransactionDTO(Integer id, String type, String description, LocalDate createdAt,
                             Double amount, String accountName) {}
