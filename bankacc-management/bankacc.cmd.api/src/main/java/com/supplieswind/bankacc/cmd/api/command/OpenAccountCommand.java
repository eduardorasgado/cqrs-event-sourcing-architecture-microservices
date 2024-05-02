package com.supplieswind.bankacc.cmd.api.command;

import com.supplieswind.bankacc.core.model.AccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class OpenAccountCommand {
    @TargetAggregateIdentifier
    private String id;

    @NotNull(message = "No account holder id was supplied")
    private String accountHolderId;

    @NotNull(message = "No account type was supplied")
    private AccountType accountType;

    @Min(value = 0, message = "Opening balance must be at least $0")
    private double openingBalance;
}
