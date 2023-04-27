package com.csv.todb.PersonData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Address")
    void deleteAllAddresses();
}
