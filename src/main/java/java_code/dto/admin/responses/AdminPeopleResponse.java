package java_code.dto.admin.responses;

import java_code.dto.admin.AdminPersonDTO;

import java.util.List;

public record AdminPeopleResponse (List<AdminPersonDTO> people){}
