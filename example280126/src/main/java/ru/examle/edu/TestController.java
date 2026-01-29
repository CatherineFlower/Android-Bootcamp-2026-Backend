package ru.examle.edu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.examle.edu.enity.*;
import ru.examle.edu.repository.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final DepartmentRepository departmentRepository;
    private final PersonRepository personRepository;
    private final MeetingRepository meetingRepository;
    private final InvitationRepository invitationRepository;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is running!");
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingRepository.findAll();
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<Invitation>> getAllInvitations() {
        List<Invitation> invitations = invitationRepository.findAll();
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStatistics() {
        long deptCount = departmentRepository.count();
        long personCount = personRepository.count();
        long meetingCount = meetingRepository.count();
        long invitationCount = invitationRepository.count();

        String stats = String.format(
                "Statistics:\n" +
                        "- Departments: %d\n" +
                        "- Persons: %d\n" +
                        "- Meetings: %d\n" +
                        "- Invitations: %d",
                deptCount, personCount, meetingCount, invitationCount
        );

        return ResponseEntity.ok(stats);
    }
}