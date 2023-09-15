package java_code.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;

public record PersonDTO (@NotEmpty String username, @JsonIgnore @NotEmpty String password){}

