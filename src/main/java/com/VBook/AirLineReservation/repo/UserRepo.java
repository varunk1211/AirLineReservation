package com.VBook.AirLineReservation.repo;

import com.VBook.AirLineReservation.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}
