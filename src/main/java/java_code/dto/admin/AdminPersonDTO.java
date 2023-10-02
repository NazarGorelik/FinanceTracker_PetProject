package java_code.dto.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

public record AdminPersonDTO (Integer id, String username, @JsonIgnore String password, LocalDateTime createdAt,
                              String role, List<AdminAccountDTO> accounts){}

