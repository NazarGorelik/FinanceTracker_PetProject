package java_code.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TransactionDTO(@NotEmpty String type, String description,
                             @Min(value = 0) @NotNull Double amount) {}
