package com.supplieswind.bankacc.query.api.handler;

import com.supplieswind.bankacc.query.api.dto.AccountLookupResponse;
import com.supplieswind.bankacc.query.api.query.FindAccountByHolderIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountByIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountsWithBalanceQuery;
import com.supplieswind.bankacc.query.api.query.FindAllAccountsQuery;

public interface AccountQueryHandler {
    AccountLookupResponse on(FindAllAccountsQuery query);
    AccountLookupResponse on(FindAccountByIdQuery query);
    AccountLookupResponse on(FindAccountByHolderIdQuery query);
    AccountLookupResponse on(FindAccountsWithBalanceQuery query);
}
