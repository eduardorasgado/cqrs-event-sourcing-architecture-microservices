package com.supplieswind.bankacc.query.api.controller;

import com.supplieswind.bankacc.query.api.dto.AccountLookupResponse;
import com.supplieswind.bankacc.query.api.dto.EqualityType;
import com.supplieswind.bankacc.query.api.query.FindAccountByHolderIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountByIdQuery;
import com.supplieswind.bankacc.query.api.query.FindAccountsWithBalanceQuery;
import com.supplieswind.bankacc.query.api.query.FindAllAccountsQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping(path = "/api/v1/bank-account")
@RequiredArgsConstructor
@Validated
public class AccountLookupController {

    private static final Logger log = LogManager.getLogger(AccountLookupController.class);

    private final QueryGateway queryGateway;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> findAllAccounts(
            @RequestParam(value = "equalityType", required = false) EqualityType equalityType,
            @RequestParam(value = "balance", required = false) Double balance) {

        if(nonNull(equalityType) && nonNull(balance)) {
            return findAccountsWithBalance(equalityType, balance);
        }

        try {
            return ResponseEntity.ok(
                    queryGateway
                            .query(new FindAllAccountsQuery(), ResponseTypes.instanceOf(AccountLookupResponse.class))
                            .join());
        } catch (final Exception ex) {
            var safeErrorMessage = "Failed to complete find all accounts request";
            log.error(safeErrorMessage);

            return ResponseEntity.internalServerError().body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    private ResponseEntity<AccountLookupResponse> findAccountsWithBalance(
            EqualityType equalityType, Double balance) {
        try {
            var response = queryGateway
                    .query(FindAccountsWithBalanceQuery.builder()
                            .equalityType(equalityType)
                            .balance(balance).build(),
                            ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            var safeErrorMessage = "Failed to complete find accounts with balance request";
            log.error(safeErrorMessage);

            return ResponseEntity.internalServerError().body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> findAccountByIdQuery(
            @Valid @NotBlank(message = "Id is mandatory") @PathVariable(value = "id") String id) {
        try {
            var response = queryGateway
                    .query(new FindAccountByIdQuery(id), ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if(isNull(response) || isNull(response.getAccounts())) {
                throw new RuntimeException();
            }

            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            if(ex.getCause() instanceof NoSuchElementException) {
                return ResponseEntity.notFound().build();
            } else {
                var safeErrorMessage = "Failed to complete find account by id request";
                log.error(safeErrorMessage);

                return ResponseEntity.internalServerError().body(new AccountLookupResponse(safeErrorMessage));
            }
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> findAccountByAccountHolderId(
            @Valid @NotBlank(message = "Account holder id is mandatory") @RequestParam(value = "accountHolderId") String id) {
        try {
            var response = queryGateway
                    .query(new FindAccountByHolderIdQuery(id), ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if(isNull(response) || isNull(response.getAccounts())) {
                throw new RuntimeException();
            }

            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            if(ex.getCause() instanceof NoSuchElementException) {
                return ResponseEntity.notFound().build();
            } else {
                var safeErrorMessage = "Failed to complete find account by account holder id request";
                log.error(safeErrorMessage);

                return ResponseEntity.internalServerError().body(new AccountLookupResponse(safeErrorMessage));
            }
        }
    }
}
