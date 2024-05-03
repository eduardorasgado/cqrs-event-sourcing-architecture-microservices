package com.supplieswind.bankacc.cmd.api.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseAccountCommand {
    @TargetAggregateIdentifier
    private String id;
}
