package java_code.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountDTO (@NotNull @Min(value = 0) Double balance, @NotEmpty String name) {}
