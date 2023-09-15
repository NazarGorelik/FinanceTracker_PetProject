package java_code.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

public record AdminAccountDTO (Integer id, Double balance, LocalDateTime createdAt, String name, String owner,
                               List<AdminTransactionDTO> transactions) {}
