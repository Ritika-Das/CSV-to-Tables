package com.csv.todb.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csv.todb.Entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
}