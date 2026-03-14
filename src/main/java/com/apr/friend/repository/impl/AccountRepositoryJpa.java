package com.apr.friend.repository.impl;

import com.apr.friend.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryJpa extends JpaRepository<Account, Long> {
}
