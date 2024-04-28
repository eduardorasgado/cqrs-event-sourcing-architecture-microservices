package com.supplieswind.user.cmd.api.controllers;

import com.supplieswind.user.cmd.api.commands.UpdateUserCommand;
import com.supplieswind.user.core.dto.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UpdateUserController {
    private static final Logger log = LogManager.getLogger(UpdateUserController.class);

    private final CommandGateway commandGateway;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> put(
            @Valid @NotBlank(message = "Id is mandatory") @PathVariable(value = "id") final String id,
            @Valid @RequestBody final UpdateUserCommand command) {

        try {
            command.setId(id);
            commandGateway.sendAndWait(command);

            return ResponseEntity.ok(new BaseResponse("The user has been updated successfully"));
        } catch (final Exception ex) {
            var safeErrorMessage = StringUtils.join(
                    "Error while processing update user request for id - ",
                    command.getId());

            log.error(safeErrorMessage);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
