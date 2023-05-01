package com.csv.todb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csv.todb.Entity.Age;

public interface AgeRepository extends JpaRepository<Age, Long> {
}