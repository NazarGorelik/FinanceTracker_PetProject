package java_code.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

public record AdminPersonDTO (int id, String username, String password, LocalDateTime created_at,
                              String role, List<AdminAccountDTO> accounts){}

