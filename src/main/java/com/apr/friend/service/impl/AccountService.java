package com.apr.friend.service.impl;

import com.apr.context.identity.LongIdGenerator;
import com.apr.friend.domain.Account;
import com.apr.friend.repository.impl.AccountRepositoryJpa;
import com.apr.friend.service.vo.AccountCreateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepositoryJpa accountRepository;

    public AccountCreateResult createAccount(String name) {
        return AccountCreateResult.fromEntity(
                accountRepository.save(
                        new Account(LongIdGenerator.nextId(), name)
                )
        );
    }
}
