package com.supplieswind.bankacc.cmd.api.controller;

import com.supplieswind.bankacc.cmd.api.command.CloseAccountCommand;
import com.supplieswind.bankacc.core.dto.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/bank-account")
@RequiredArgsConstructor
@Validated
public class CloseAccountController {

    private static final Logger log = LogManager.getLogger(CloseAccountController.class);

    private final CommandGateway commandGateway;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> delete(
            @Valid @NotBlank(message = "Id is mandatory") @PathVariable(value = "id") final String id) {
        var command = new CloseAccountCommand(id);

        try {
            commandGateway.sendAndWait(command);
            return ResponseEntity.noContent().build();
        } catch (final Exception ex) {
            var safeErrorMessage = StringUtils.join(
                    "Error while processing remove bank account request for id: ", id);
            log.error(safeErrorMessage);

            return ResponseEntity.internalServerError().body(new BaseResponse(safeErrorMessage));
        }
    }
}
