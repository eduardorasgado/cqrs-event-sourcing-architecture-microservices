package com.supplieswind.bankacc.cmd.api.command;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class WithdrawFundsCommand {
    @TargetAggregateIdentifier
    private String id;

    @Min(value = 1, message = "The withdraw amount must be greater than $0")
    private double amount;
}
