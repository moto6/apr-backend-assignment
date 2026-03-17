package com.apr.friend.service.vo;

import com.apr.friend.domain.Account;

public record AccountCreateResult(
        Long accountId,
        String name
) {
    public static AccountCreateResult fromEntity(Account account) {
        return new AccountCreateResult(
                account.getAccountId(),
                account.getName()
        );
    }
}
