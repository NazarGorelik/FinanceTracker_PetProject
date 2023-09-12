package java_code.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.admin.responses.AdminAccountsResponse;
import java_code.dto.admin.responses.AdminPeopleResponse;
import java_code.mappers.AccountMapper;
import java_code.mappers.PersonMapper;
import java_code.services.AccountService;
import java_code.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Hidden
public class AdminController {

    private final PersonService personService;
    private final AccountService accountService;

    @GetMapping("/allPeople")
    public AdminPeopleResponse getAllPeople(){
        List<AdminPersonDTO> adminPersonDTOS = personService.findAll().stream()
                .map(x-> PersonMapper.INSTANCE.toAdminPersonDTO(x)).collect(Collectors.toList());
        return new AdminPeopleResponse(adminPersonDTOS);
    }

    @GetMapping("/allAccounts")
    public AdminAccountsResponse getAllAccounts(){
        List<AdminAccountDTO> adminAccountDTOS = accountService.findAll().stream()
                .map(x-> AccountMapper.INSTANCE.toAdminAccountDTO(x)).collect(Collectors.toList());
        return new AdminAccountsResponse(adminAccountDTOS);
    }

    @GetMapping("/getInfo/{username}")
    public AdminPersonDTO getPersonInfo(@PathVariable("username") String username){
        AdminPersonDTO adminPersonDTO = PersonMapper.INSTANCE.toAdminPersonDTO(personService.findByUsername(username));
        return adminPersonDTO;
    }
}
