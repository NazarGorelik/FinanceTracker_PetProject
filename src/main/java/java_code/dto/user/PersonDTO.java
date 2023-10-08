package java_code.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record PersonDTO (@NotEmpty String username, @NotEmpty String password){}

