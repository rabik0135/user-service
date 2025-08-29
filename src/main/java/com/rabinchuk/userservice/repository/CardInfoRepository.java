package com.rabinchuk.userservice.repository;

import com.rabinchuk.userservice.model.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {

    @Override
    @Query("""
            SELECT c
            FROM CardInfo c
            WHERE c.id = :id
            """)
    Optional<CardInfo> findById(@Param("id") Long id);

    @Query(value = """
            SELECT *
            FROM card_info c
            WHERE c.user_id = :userId
            """, nativeQuery = true)
    List<CardInfo> findByUserId(@Param("userId") Long userId);
}
