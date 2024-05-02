package com.supplieswind.bankacc.cmd.api.aggregate;

import com.supplieswind.bankacc.cmd.api.command.CloseAccountCommand;
import com.supplieswind.bankacc.cmd.api.command.DepositFundsCommand;
import com.supplieswind.bankacc.cmd.api.command.OpenAccountCommand;
import com.supplieswind.bankacc.cmd.api.command.WithdrawFundsCommand;
import com.supplieswind.bankacc.core.event.AccountClosedEvent;
import com.supplieswind.bankacc.core.event.AccountOpenedEvent;
import com.supplieswind.bankacc.core.event.FundsDepositedEvent;
import com.supplieswind.bankacc.core.event.FundsWithdrawnEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
@NoArgsConstructor
public class AccountAggregate {

    @AggregateIdentifier
    private String id;

    private String accountHolderId;

    private double balance;

    @CommandHandler
    public AccountAggregate(OpenAccountCommand command) {
        var event = AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolderId(command.getAccountHolderId())
                .accountType(command.getAccountType())
                .creationDate(new Date())
                .openingBalance(command.getOpeningBalance())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvent event) {
        id = event.getId();
        accountHolderId = event.getAccountHolderId();
        balance = event.getOpeningBalance();
    }

    @CommandHandler
    public void handle(DepositFundsCommand command) {
        var amount = command.getAmount();

        var event = FundsDepositedEvent.builder()
                .id(command.getId())
                .amount(amount)
                .balance(balance + amount)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(FundsDepositedEvent event) {
        balance += event.getAmount();
    }

    @CommandHandler
    public void handle(WithdrawFundsCommand command) {
        var amount = command.getAmount();
        var newBalance = balance - amount;

        if(newBalance < 0) {
            throw new IllegalStateException("Withdrawal declined, insufficient funds.");
        }

        var event = FundsWithdrawnEvent.builder()
                .id(command.getId())
                .amount(amount)
                .balance(newBalance)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(FundsWithdrawnEvent event) {
        balance -= event.getAmount();
    }

    @CommandHandler
    public void handle(CloseAccountCommand command) {
        var event = AccountClosedEvent.builder()
                .id(command.getId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(AccountClosedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
