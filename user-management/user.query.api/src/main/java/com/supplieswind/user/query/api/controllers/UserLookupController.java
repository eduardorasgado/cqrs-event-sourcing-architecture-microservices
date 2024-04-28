package com.supplieswind.user.query.api.controllers;

import com.supplieswind.user.query.api.dto.UserLookupResponse;
import com.supplieswind.user.query.api.queries.FindAllUsersQuery;
import com.supplieswind.user.query.api.queries.FindUserByIdQuery;
import com.supplieswind.user.query.api.queries.SearchUsersQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserLookupController {

    private static final Logger log = LogManager.getLogger(UserLookupController.class);

    private final QueryGateway queryGateway;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookupResponse> findAllUsers() {
        try {
          UserLookupResponse response = queryGateway.query(new FindAllUsersQuery(),
                                                            ResponseTypes.instanceOf(UserLookupResponse.class))
                                                    .join();
          if(isNull(response) || isNull(response.getUsers())) {
              throw new RuntimeException();
          }

          return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            var safeErrorMessage = "Failed to complete get all users request";
            log.error(safeErrorMessage);

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookupResponse> getUserById(
            @Valid @NotBlank
            @PathVariable(value = "id") String id) {
        try {
            UserLookupResponse response = queryGateway.query(new FindUserByIdQuery(id),
                            ResponseTypes.instanceOf(UserLookupResponse.class))
                    .join();
            if(isNull(response) || isNull(response.getUsers())) {
                throw new RuntimeException();
            }

            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            if(ex.getCause() instanceof NoSuchElementException) {
                return ResponseEntity.notFound().build();
            } else {
                var safeErrorMessage = "Failed to complete find user by id request";
                log.error(safeErrorMessage);

                return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookupResponse> searchUsers(
            @Valid @NotBlank
            @RequestParam(value = "filter") String filter) {
        try {
            UserLookupResponse response = queryGateway.query(new SearchUsersQuery(filter),
                            ResponseTypes.instanceOf(UserLookupResponse.class))
                    .join();
            if(isNull(response) || isNull(response.getUsers())) {
                throw new RuntimeException();
            }

            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            var safeErrorMessage = "Failed to complete search users request";
            log.error("{}: {}", safeErrorMessage, ex);

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
