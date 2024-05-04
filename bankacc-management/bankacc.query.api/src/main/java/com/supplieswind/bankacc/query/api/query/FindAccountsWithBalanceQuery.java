package com.supplieswind.bankacc.query.api.query;

import com.supplieswind.bankacc.query.api.dto.EqualityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FindAccountsWithBalanceQuery {
    private EqualityType equalityType;
    private double balance;
}
