package java_code.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.admin.responses.AdminAccountsResponse;
import java_code.dto.admin.responses.AdminPeopleResponse;
import java_code.services.AccountService;
import java_code.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Hidden
public class AdminController {

    private final PersonService personService;
    private final AccountService accountService;

    @GetMapping("/allPeople")
    public ResponseEntity<List<AdminPersonDTO>> getAllPeople(){
        return new ResponseEntity<>(personService.findAllAdminPersonDTOs(), HttpStatus.OK);
    }

    @GetMapping("/allAccounts")
    public AdminAccountsResponse getAllAccounts(){
        return new AdminAccountsResponse(accountService.findAllAdminAccountDTOs());
    }

    @GetMapping("/getInfo/{username}")
    public AdminPersonDTO getPersonInfo(@PathVariable("username") String username){
        return personService.findAdminPersonDTOByUsername(username);
    }
}
