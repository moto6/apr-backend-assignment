package com.apr.friend.controller;


import com.apr.context.web.ApiResponse;
import com.apr.friend.controller.dto.AccountCreateRequest;
import com.apr.friend.service.impl.AccountService;
import com.apr.friend.service.vo.AccountCreateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account API", description = "계정 관리 API")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "신규 계정 생성", description = "이름을 입력받아 새로운 계정을 생성합니다.")
    @PostMapping
    public ApiResponse<AccountCreateResult> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return ApiResponse.ok(
                accountService.createAccount(request.name())
        );
    }
}
