package com.melhamra.api.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "users")
@Data
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userID;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;

    @Column(nullable = false)
    private Boolean emailVerificationStatus = false;

}
