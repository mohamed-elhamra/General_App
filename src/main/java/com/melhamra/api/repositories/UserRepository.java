package com.melhamra.api.repositories;

import com.melhamra.api.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserID(String userID);

    @Query(value = "select * from users", nativeQuery = true)
    Page<UserEntity> findAllUsers(Pageable pageable);

    @Query("select u from users u where u.firstName like %:search% or u.lastName like %:search%")
    Page<UserEntity> findAllUsersByCriteria(Pageable pageable, @Param("search") String search);

}
