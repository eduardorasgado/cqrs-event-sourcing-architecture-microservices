package com.supplieswind.user.cmd.api.controllers;

import com.supplieswind.user.cmd.api.commands.RegisterUserCommand;
import com.supplieswind.user.cmd.api.dto.RegisterUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class RegisterUserController {
    private static final Logger log = LogManager.getLogger(RegisterUserController.class);
    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<RegisterUserResponse> post(@Valid @RequestBody RegisterUserCommand command) {
        command.setId(UUID.randomUUID().toString());

        try {
            commandGateway.sendAndWait(command);

            return ResponseEntity.created(URI.create("/api/v1/users/" + command.getId()))
                    .body(RegisterUserResponse.builder()
                            .id(command.getId())
                            .message("User successfully registered")
                            .build());
        } catch (final Exception ex) {
            var safeErrorMessage = StringUtils.join(
                    "Error while processing register user request for id - ",
                    command.getId());

            log.error(safeErrorMessage);
            return new ResponseEntity<>(RegisterUserResponse.builder()
                    .id(command.getId())
                    .message(safeErrorMessage)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
