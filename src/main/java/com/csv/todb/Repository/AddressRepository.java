package com.csv.todb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csv.todb.Entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}