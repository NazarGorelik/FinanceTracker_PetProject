package java_code.dto.admin.responses;

import java_code.dto.admin.AdminAccountDTO;

import java.util.List;

public record AdminAccountsResponse (List<AdminAccountDTO> list){}
