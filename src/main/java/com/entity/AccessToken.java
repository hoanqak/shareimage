package com.entity;

import com.service.base.EntityBaseService;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "access_token")
@Data
public class AccessToken extends EntityBaseService
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String accessToken;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
