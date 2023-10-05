package java_code.mappers;

import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.user.PersonDTO;
import java_code.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person toPerson(PersonDTO personDTO);

    //EXPRESSION MAPPING WITH MAPSTRUCT
    @Mapping(target = "accounts", expression = "java(mapAccounts(person))")
    AdminPersonDTO toAdminPersonDTO(Person person);

    default List<AdminAccountDTO> mapAccounts(Person person){
        if(!person.getAccounts().isEmpty())
            return person.getAccounts().stream().map(x->AccountMapper.INSTANCE.toAdminAccountDTO(x)).collect(Collectors.toList());

        return null;
    }
}
