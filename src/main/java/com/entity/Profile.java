package com.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name")
    private String fullName;
    @Column
    private String address;
    private int age;
    private Date birthday;
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

}
