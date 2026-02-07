package ru.sicampus.bootcamp2026.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.sicampus.bootcamp2026.dto.PersonDTO;
import ru.sicampus.bootcamp2026.dto.PersonRegisterDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(Long id);

    PersonDTO createPerson(PersonRegisterDTO dto);

    PersonDTO updatePerson(Long id, PersonDTO dto);

    void deletePerson(Long id);

    PersonDTO getPersonByUsername(String username);

    Page<PersonDTO> getAllPersonPaginated(Pageable page);
}
