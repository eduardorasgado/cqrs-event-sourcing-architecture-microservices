package com.supplieswind.bankacc.query.api.handler;

import com.supplieswind.bankacc.core.event.AccountClosedEvent;
import com.supplieswind.bankacc.core.event.AccountOpenedEvent;
import com.supplieswind.bankacc.core.event.FundsDepositedEvent;
import com.supplieswind.bankacc.core.event.FundsWithdrawnEvent;
import com.supplieswind.bankacc.core.model.BankAccount;
import com.supplieswind.bankacc.query.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@ProcessingGroup("bankaccount-group")
@RequiredArgsConstructor
public class AccountEventHandlerImpl implements AccountEventHandler {

    private final AccountRepository repository;

    @Override
    @EventHandler
    public void on(AccountOpenedEvent event) {
        var account = BankAccount.builder()
                .id(event.getId())
                .accountHolderId(event.getAccountHolderId())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .creationDate(event.getCreationDate())
                .build();

        repository.save(account);
    }

    @Override
    @EventHandler
    public void on(FundsDepositedEvent event) {
        updateAccountBalance(event.getId(), event.getBalance(), "Failed trying to deposit to a non existing bank account");
    }

    @Override
    @EventHandler
    public void on(FundsWithdrawnEvent event) {
        updateAccountBalance(event.getId(), event.getBalance(), "Failed trying to withdraw from a non existing bank account");
    }

    private void updateAccountBalance(final String id, final double balance, final String errorMsg) {
        Objects.requireNonNull(id, "The bank account id to update is mandatory");

        var bankAccount = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(errorMsg));

        bankAccount.setBalance(balance);
        repository.save(bankAccount);
    }

    @Override
    @EventHandler
    public void on(AccountClosedEvent event) {
        Objects.requireNonNull(event);

        var accountId = event.getId();
        Objects.requireNonNull(accountId, "The bank account to delete does not exist");

        repository.deleteById(accountId);
    }
}
