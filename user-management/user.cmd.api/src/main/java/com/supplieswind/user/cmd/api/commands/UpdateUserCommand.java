package com.supplieswind.user.cmd.api.commands;

import com.supplieswind.user.core.models.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateUserCommand {
    @TargetAggregateIdentifier
    private String id;

    @Valid
    @NotNull(message = "No user details were supplied")
    private User user;
}
