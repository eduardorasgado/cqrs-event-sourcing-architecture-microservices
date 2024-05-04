package com.supplieswind.bankacc.query.api.handler;

import com.supplieswind.bankacc.core.model.BankAccount;
import com.supplieswind.bankacc.query.api.dto.AccountLookupResponse;
import com.supplieswind.bankacc.query.api.dto.EqualityType;
import com.supplieswind.bankacc.query.api.query.FindAccountByHolderIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountByIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountsWithBalanceQuery;
import com.supplieswind.bankacc.query.api.query.FindAllAccountsQuery;
import com.supplieswind.bankacc.query.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class AccountQueryHandlerImpl implements AccountQueryHandler {

    private final AccountRepository repository;

    @Override
    @QueryHandler
    public AccountLookupResponse on(FindAllAccountsQuery query) {
        var accounts = repository.findAll();

        var accountList = new ArrayList<BankAccount>();
        accounts.forEach(accountList::add);

        return new AccountLookupResponse(accountList);
    }

    @Override
    @QueryHandler
    public AccountLookupResponse on(FindAccountByIdQuery query) {
        if(isNull(query) || isNull(query.getId())) {
            throw new IllegalArgumentException();
        }

        var id = query.getId();
        var account = repository.findById(id).orElseThrow(NoSuchElementException::new);

        return new AccountLookupResponse(account);
    }

    @Override
    @QueryHandler
    public AccountLookupResponse on(FindAccountByHolderIdQuery query) {
        if(isNull(query) || isNull(query.getAccountHolderId())) {
            throw new IllegalArgumentException();
        }

        var id = query.getAccountHolderId();
        var account = repository.findByAccountHolderId(id).orElseThrow(NoSuchElementException::new);

        return new AccountLookupResponse(account);
    }

    @Override
    @QueryHandler
    public AccountLookupResponse on(FindAccountsWithBalanceQuery query) {
        if(isNull(query) || isNull(query.getEqualityType())) {
            throw new IllegalArgumentException();
        }

        var equalityType = query.getEqualityType();
        var balance = query.getBalance();
        List<BankAccount> accounts = null;

        if(equalityType.equals(EqualityType.LESS_THAN)) {
            accounts = repository.findByBalanceLessThan(balance);
        } else if(equalityType.equals(EqualityType.GREATER_THAN)) {
            accounts = repository.findByBalanceGreaterThan(balance);
        }

        return new AccountLookupResponse(accounts);
    }
}
