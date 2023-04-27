package com.csv.todb.PersonData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleDataRepository extends JpaRepository<People, String> {

}