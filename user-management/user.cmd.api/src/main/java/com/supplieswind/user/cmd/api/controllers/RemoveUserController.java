package com.supplieswind.user.cmd.api.controllers;

import com.supplieswind.user.cmd.api.commands.RemoveUserCommand;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Validated
public class RemoveUserController {
    private static final Logger log = LogManager.getLogger(RemoveUserController.class);

    private final CommandGateway commandGateway;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@Valid @NotBlank(message = "Id is mandatory")
                                        @PathVariable(value = "id") String id) {
        var command = new RemoveUserCommand(id);

        try {
            commandGateway.sendAndWait(command);
            return ResponseEntity.noContent().build();
        } catch (final Exception ex) {
            var safeErrorMessage = StringUtils.join(
                    "Error while processing remove user request for id - ",
                    command.getId());

            log.error(safeErrorMessage);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
