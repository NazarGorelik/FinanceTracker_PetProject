package java_code.services;

import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.security.UserPrincipal;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final PersonRepository personRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);

        if (!person.isPresent())
            throw new PersonNotFoundException("Person is not found!!");

        return new UserPrincipal(person.get());
    }
}
