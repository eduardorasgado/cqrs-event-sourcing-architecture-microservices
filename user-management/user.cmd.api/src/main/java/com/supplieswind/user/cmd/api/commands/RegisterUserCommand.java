package com.supplieswind.user.cmd.api.commands;

import com.supplieswind.user.core.models.User;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RegisterUserCommand {
    @TargetAggregateIdentifier
    private String id;

    private User user;
}
