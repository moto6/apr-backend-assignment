package com.apr.friend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    private Long accountId;

    private String name;
}
