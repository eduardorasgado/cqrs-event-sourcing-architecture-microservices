package com.supplieswind.bankacc.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsDepositedEvent {
    private String id;
    private double amount;
    private double balance;
}
