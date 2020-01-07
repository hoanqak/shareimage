package com.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileDTO
{
    private int id;
    private String fullName;
    private String address;
    private int age;
    private Date birthday;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
}
