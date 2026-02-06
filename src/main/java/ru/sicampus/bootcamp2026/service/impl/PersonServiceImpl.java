package ru.sicampus.bootcamp2026.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sicampus.bootcamp2026.dto.PersonDTO;
import ru.sicampus.bootcamp2026.entity.Department;
import ru.sicampus.bootcamp2026.entity.Person;
import ru.sicampus.bootcamp2026.exception.DepartmentNotFoundException;
import ru.sicampus.bootcamp2026.exception.PersonNotFoundException;
import ru.sicampus.bootcamp2026.repository.DepartmentRepository;
import ru.sicampus.bootcamp2026.repository.PersonRepository;
import ru.sicampus.bootcamp2026.service.PersonService;
import ru.sicampus.bootcamp2026.util.PersonMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository PersonRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<PersonDTO> getAllPersons() {
        return PersonRepository.findAll().stream()
                .map(PersonMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        return PersonRepository.findById(id)
                .map(PersonMapper::convertToDto)
                .orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public PersonDTO createPerson(PersonDTO dto) {
        Optional<Department> optionalDepartment = departmentRepository.findByName(dto.getDepartmentName());
        if(optionalDepartment.isEmpty()){
            throw new DepartmentNotFoundException();
        }

        Person Person = new Person();
        Person.setName(dto.getName());
        Person.setEmail(dto.getEmail());
        Person.setPhotoUrl(dto.getPhotoUrl());
        Person.setDepartment(optionalDepartment.get());

        return PersonMapper.convertToDto(PersonRepository.save(Person));
    }

    @Override
    public PersonDTO updatePerson(Long id, PersonDTO dto) {
        Person Person = PersonRepository.findById(id)
                .orElseThrow(PersonNotFoundException::new);

        Person.setName(dto.getName());
        Person.setEmail(dto.getEmail());
        Person.setPhotoUrl(dto.getPhotoUrl());

        Optional<Department> optionalDepartment = departmentRepository.findByName(dto.getDepartmentName());
        optionalDepartment.ifPresent(Person::setDepartment);

        return PersonMapper.convertToDto(PersonRepository.save(Person));
    }

    @Override
    public void deletePerson(Long id) {
        PersonRepository.deleteById(id);
    }
}
