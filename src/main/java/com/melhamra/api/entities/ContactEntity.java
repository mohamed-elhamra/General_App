package com.melhamra.api.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "contacts")
@Data
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column(length = 32)
    private String contactId;
    @NotBlank
    private String mobile;
    private String skype;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
