package com.entity;

import com.service.EntityBaseService;
import lombok.Data;

import javax.persistence.*;

@Table(name = "user")
@Entity
@Data
public class User extends EntityBaseService
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    private String password;
    private String email;
    private boolean active;
}
