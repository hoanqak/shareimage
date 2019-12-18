package com.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "image")
@Data
public class Image implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "file_name")
    private String fileName;
    @Column
    private int size;
    byte[] image;
}
