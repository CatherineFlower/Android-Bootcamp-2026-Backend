package ru.sicampus.bootcamp2026.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sicampus.bootcamp2026.dto.PersonDTO;
import ru.sicampus.bootcamp2026.dto.PersonRegisterDTO;
import ru.sicampus.bootcamp2026.entity.Authority;
import ru.sicampus.bootcamp2026.entity.Department;
import ru.sicampus.bootcamp2026.entity.Person;
import ru.sicampus.bootcamp2026.exception.DepartmentNotFoundException;
import ru.sicampus.bootcamp2026.exception.PersonAlreadyExistsException;
import ru.sicampus.bootcamp2026.exception.PersonNotFoundException;
import ru.sicampus.bootcamp2026.repository.AuthorityRepository;
import ru.sicampus.bootcamp2026.repository.DepartmentRepository;
import ru.sicampus.bootcamp2026.repository.PersonRepository;
import ru.sicampus.bootcamp2026.service.PersonService;
import ru.sicampus.bootcamp2026.util.PersonMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(PersonMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        return personRepository.findById(id)
                .map(PersonMapper::convertToDto)
                .orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public PersonDTO createPerson(PersonRegisterDTO dto) {
        if (personRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new PersonAlreadyExistsException();
        }

        Optional<Department> optionalDepartment = departmentRepository.findByName(dto.getDepartmentName());
        if (optionalDepartment.isEmpty()) {
            throw new DepartmentNotFoundException();
        }

        Optional<Authority> roleUser = authorityRepository.findByAuthority("ROLE_USER");
        if (roleUser.isEmpty()) {
            throw new PersonNotFoundException();
        }

        Person person = new Person();
        person.setName(dto.getName());
        person.setUsername(dto.getUsername());
        person.setEmail(dto.getEmail());
        person.setDepartment(optionalDepartment.get());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setAuthorities(Set.of(roleUser.get()));

        return PersonMapper.convertToDto(personRepository.save(person));
    }

    @Override
    public PersonDTO updatePerson(Long id, PersonDTO dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(PersonNotFoundException::new);

        if (personRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new PersonAlreadyExistsException("Username already exists!");
        }

        person.setName(dto.getName());
        person.setUsername(dto.getUsername());
        person.setEmail(dto.getEmail());
        person.setPhotoUrl(dto.getPhotoUrl());

        Optional<Department> optionalDepartment = departmentRepository.findByName(dto.getDepartmentName());
        optionalDepartment.ifPresent(person::setDepartment);

        return PersonMapper.convertToDto(personRepository.save(person));
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public PersonDTO getPersonByUsername(String username) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);

        if (optionalPerson.isEmpty()) {
            throw new PersonNotFoundException("Person with username" + username + "name not found!");
        }

        return PersonMapper.convertToDto(optionalPerson.get());
    }
}
