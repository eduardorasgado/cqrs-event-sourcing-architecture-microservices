package com.supplieswind.bankacc.cmd.api.controller;

import com.supplieswind.bankacc.cmd.api.command.OpenAccountCommand;
import com.supplieswind.bankacc.cmd.api.dto.OpenAccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/bank-account")
@RequiredArgsConstructor
public class OpenAccountController {

    private static final Logger log = LogManager.getLogger(OpenAccountController.class);

    private final CommandGateway commandGateway;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<OpenAccountResponse> post(@Valid @RequestBody OpenAccountCommand command) {
        command.setId(UUID.randomUUID().toString());

        try {
            commandGateway.sendAndWait(command);

            return ResponseEntity.created(URI.create("/api/v1/bank-account/" + command.getId()))
                    .body(OpenAccountResponse.builder()
                            .id(command.getId())
                            .message("Bank account successfully created")
                            .build());
        } catch (final Exception ex) {
            var safeErrorMessage = "Error while processing open account request";

            log.error(safeErrorMessage);
            return ResponseEntity.internalServerError()
                    .body(OpenAccountResponse.builder()
                            .message(safeErrorMessage)
                            .build());
        }
    }
}
