package com.supplieswind.bankacc.query.api.handler;

import com.supplieswind.bankacc.core.event.AccountClosedEvent;
import com.supplieswind.bankacc.core.event.AccountOpenedEvent;
import com.supplieswind.bankacc.core.event.FundsDepositedEvent;
import com.supplieswind.bankacc.core.event.FundsWithdrawnEvent;

public interface AccountEventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
    void on(AccountClosedEvent event);
}
