package ru.sicampus.bootcamp2026.service;

import ru.sicampus.bootcamp2026.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(Long id);

    PersonDTO createPerson(PersonDTO dto);

    PersonDTO updatePerson(Long id, PersonDTO dto);

    void deletePerson(Long id);
}
