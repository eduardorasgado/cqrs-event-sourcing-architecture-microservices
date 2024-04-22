package com.supplieswind.user.query.api.controllers;

import com.supplieswind.user.query.api.dto.UserLookupResponse;
import com.supplieswind.user.query.api.queries.FindAllUsersQuery;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserLookupController {

    private static final Logger log = LogManager.getLogger(UserLookupController.class);

    private final QueryGateway queryGateway;

    @GetMapping
    public ResponseEntity<UserLookupResponse> findAllUsers() {
        try {
          UserLookupResponse response = queryGateway.query(new FindAllUsersQuery(),
                                                            ResponseTypes.instanceOf(UserLookupResponse.class))
                                                    .join();
          if(isNull(response) || isNull(response.getUsers())) {
              throw new RuntimeException("Failed trying to get all the users");
          }

          return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            var safeErrorMessage = "Failed to complete get all users request";
            log.error(safeErrorMessage);

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
