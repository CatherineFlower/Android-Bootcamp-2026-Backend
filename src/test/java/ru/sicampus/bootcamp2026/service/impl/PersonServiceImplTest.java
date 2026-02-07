package ru.sicampus.bootcamp2026.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sicampus.bootcamp2026.dto.PersonDTO;
import ru.sicampus.bootcamp2026.dto.PersonRegisterDTO;
import ru.sicampus.bootcamp2026.entity.Authority;
import ru.sicampus.bootcamp2026.entity.Department;
import ru.sicampus.bootcamp2026.entity.Person;
import ru.sicampus.bootcamp2026.exception.DepartmentNotFoundException;
import ru.sicampus.bootcamp2026.exception.PersonAlreadyExistsException;
import ru.sicampus.bootcamp2026.repository.AuthorityRepository;
import ru.sicampus.bootcamp2026.repository.DepartmentRepository;
import ru.sicampus.bootcamp2026.repository.PersonRepository;
import ru.sicampus.bootcamp2026.util.PersonMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    void testRegisterNewUser() {
        // Given
        PersonRegisterDTO dto = new PersonRegisterDTO();
        dto.setUsername("testUser");
        dto.setEmail("test@mail.com");
        dto.setName("Test User");
        dto.setPassword("123");
        dto.setDepartmentName("IT");

        Department department = new Department();
        department.setName("IT");

        Person person = new Person();
        person.setUsername("testUser");
        person.setEmail("test@mail.com");
        person.setName("Test User");
        person.setDepartment(department);

        Authority roleUser = new Authority();
        roleUser.setAuthority("ROLE_USER");


        // Mocks
        when(personRepository.findByUsername("testUser"))
                .thenReturn(Optional.empty());

        when(departmentRepository.findByName("IT"))
                .thenReturn(Optional.of(department));

        when(authorityRepository.findByAuthority("ROLE_USER"))
                .thenReturn(Optional.of(roleUser));

        when(passwordEncoder.encode("123"))
                .thenReturn("encodedPassword");

        when(personRepository.save(any(Person.class)))
                .thenReturn(person);


        // When
        PersonDTO result = personService.createPerson(dto);

        // Then
        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        assertEquals("IT", result.getDepartmentName());
    }

    @Test
    void testRegisterExistingUsername(){
        // Given
        PersonRegisterDTO dto = new PersonRegisterDTO();
        dto.setUsername("existingUser");
        dto.setPassword("123");
        dto.setDepartmentName("IT");

        Person existingPerson = new Person();

        // Mock
        when(personRepository.findByUsername("existingUser"))
                .thenReturn(Optional.of(existingPerson));

        // When & Then
        assertThrows(PersonAlreadyExistsException.class,
                () -> personService.createPerson(dto));

        verify(personRepository, never()).save(any());
    }

    @Test
    void testRegisterNonExistentDepartment(){
        // Given
        PersonRegisterDTO dto = new PersonRegisterDTO();
        dto.setUsername("newUser");
        dto.setDepartmentName("NonExistentDept");

        // Mock
        when(personRepository.findByUsername("newUser"))
                .thenReturn(Optional.empty());

        when(departmentRepository.findByName("NonExistentDept"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(DepartmentNotFoundException.class,
                () -> personService.createPerson(dto));
    }
}
