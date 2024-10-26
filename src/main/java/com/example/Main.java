package com.example;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {

  record Teacher(String name) {}

  record Subject(String name, Teacher teacher) {}

  record Student(String name, List<Subject> subjects) {}

  private static List<Student> students;

  public static void main(String[] args) {
    fillData();

    //Get all teachers
    getAllTeachers();

    //Group students by subjects
    //groupStudentsBySubjects();

    //Group students by teachers
  }

  private static void getAllTeachers() {
    students.stream()
        .map(Student::subjects)
        .flatMap(Collection::stream)
        .map(Subject::teacher)
        .map(Teacher::name)
        .distinct()
        .forEach(System.out::println);
  }

  private static void groupStudentsBySubjects() {
    record StudentSubject(Student student, Subject subject) {}
    Map<Subject, List<Student>> subjectToStudents = students.stream()
        .flatMap(student -> student.subjects.stream()
            .map(subject -> new StudentSubject(student, subject)))
        .collect(Collectors.groupingBy(
            StudentSubject::subject,
            Collectors.mapping(StudentSubject::student, Collectors.toList())));

    Map<String, List<String>> subjectNameToStudentsNames = subjectToStudents.entrySet().stream()
        .map(entry ->
            Map.entry(
                entry.getKey().name(),
                entry.getValue().stream().map(Student::name).toList()))
        .collect(Collectors.groupingBy(
            Entry::getKey,
            Collectors.flatMapping(entry -> entry.getValue().stream(), Collectors.toList())));

    subjectNameToStudentsNames.entrySet()
        .forEach(entry ->
            System.out.printf("Subject: %s, Students: %s%n", entry.getKey(), entry.getValue()));
  }

  private static void fillData() {
    Teacher teacherJohn = new Teacher("John");
    Teacher teacherTom = new Teacher("Tom");
    Teacher teacherJane = new Teacher("Jane");

    Subject subjectMathJon = new Subject("Math", teacherJohn);
    Subject subjectMathTom = new Subject("Math", teacherTom);
    Subject subjectHistoryJane = new Subject("History", teacherJane);

    Student studentBob = new Student("Bob", List.of(subjectMathJon, subjectHistoryJane));
    Student studentJessica = new Student("Jessica", List.of(subjectMathTom, subjectHistoryJane));
    Student studentGary = new Student("Gary", List.of(subjectMathTom));
    Student studentLex = new Student("Lex", List.of(subjectHistoryJane));

    students = List.of(studentBob, studentJessica, studentGary, studentLex);
  }
}