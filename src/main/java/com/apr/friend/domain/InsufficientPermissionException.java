package com.apr.friend.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InsufficientPermissionException extends RuntimeException {
    private static final String MESSAGE = "해당 작업을 수행할 권한이 없습니다";
    private static final String LOG_FORMAT = "expectAccountId=[%d] actualAccountId=[%d]";
    private static final Logger log = LoggerFactory.getLogger(InsufficientPermissionException.class);

    public InsufficientPermissionException(Long expectAccountId, Long actualAccountId) {
        super(MESSAGE);
        log.info(String.format(LOG_FORMAT, expectAccountId, actualAccountId));
    }
}
