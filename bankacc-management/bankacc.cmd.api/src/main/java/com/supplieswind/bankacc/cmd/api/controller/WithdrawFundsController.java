package com.supplieswind.bankacc.cmd.api.controller;

import com.supplieswind.bankacc.cmd.api.command.WithdrawFundsCommand;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/bank-account")
@RequiredArgsConstructor
public class WithdrawFundsController {

    private static final Logger log = LogManager.getLogger(DepositFundsController.class);

    private final CommandGateway commandGateway;

    @PatchMapping("{id}/withdraw")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> put(
            @Valid @NotBlank(message = "Id is mandatory") @PathVariable(value = "id") final String id,
            @Valid @RequestBody WithdrawFundsCommand command) {
        command.setId(id);

        try {
            commandGateway.sendAndWait(command);
            return ResponseEntity.ok(new BaseResponse("The funds withdraw operation has been executed sucessfully"));
        } catch (final Exception ex) {
            var safeErrorMessage = StringUtils.join(
                    "Error while processing request to withdraw funds from bank account for id: ", id);
            log.error(safeErrorMessage);

            return ResponseEntity.internalServerError().body(new BaseResponse(safeErrorMessage));
        }
    }
}
