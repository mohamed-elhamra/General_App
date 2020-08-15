package com.melhamra.api.entities;

import com.melhamra.api.dtos.UserDto;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "addresses")
@Data
public class AddressEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String addressId;
    @Column(nullable = false, length = 20)
    private String city;
    @Column(nullable = false, length = 20)
    private String country;
    @Column(nullable = false, length = 50)
    private String street;
    @Column(nullable = false, length = 7)
    private String postal;
    @Column(nullable = false, length = 20)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
