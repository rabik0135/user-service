package com.rabinchuk.userservice.repository;

import com.rabinchuk.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT *
            FROM users
            WHERE email = :email
            """, nativeQuery = true)
    Optional<User> findUserByEmail(@Param("email") String email);

}
