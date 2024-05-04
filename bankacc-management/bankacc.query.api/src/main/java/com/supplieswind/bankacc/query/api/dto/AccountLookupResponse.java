package com.supplieswind.bankacc.query.api.dto;

import com.supplieswind.bankacc.core.dto.BaseResponse;
import com.supplieswind.bankacc.core.model.BankAccount;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountLookupResponse extends BaseResponse {
    private List<BankAccount> accounts;

    public AccountLookupResponse(String message) {
        super(message);
    }

    @Builder
    public AccountLookupResponse(String message, List<BankAccount> accounts) {
        super(message);
        this.accounts = accounts;
    }

    public AccountLookupResponse(List<BankAccount> accounts) {
        super();
        this.accounts = accounts;
    }

    public AccountLookupResponse(BankAccount account) {
        super();
        accounts = new ArrayList<>(Collections.singletonList(account));
    }
}
