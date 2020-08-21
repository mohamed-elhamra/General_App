package com.melhamra.api.repositories;

import com.melhamra.api.entities.AddressEntity;
import com.melhamra.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUser(UserEntity userEntity);

    AddressEntity findByAddressId(String id);

}
