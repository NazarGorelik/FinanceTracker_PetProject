package java_code.dto.user.responses;

import java_code.dto.user.AccountDTO;

import java.util.List;

public record AccountsResponse (List<AccountDTO> list){}
