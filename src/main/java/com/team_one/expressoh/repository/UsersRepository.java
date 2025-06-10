package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    //  ensures that when findByEmail(String email) is called,
    //  it retrieves the Users entity along with its associated Profile in one query.
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.profile WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);
}

