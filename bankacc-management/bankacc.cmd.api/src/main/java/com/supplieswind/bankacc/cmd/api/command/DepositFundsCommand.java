package com.supplieswind.bankacc.cmd.api.command;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class DepositFundsCommand {
    @TargetAggregateIdentifier
    private String id;

    @Min(value = 1, message = "The deposit amount must be greater than $0")
    private double amount;
}
