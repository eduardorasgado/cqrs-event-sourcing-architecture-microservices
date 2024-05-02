package com.supplieswind.bankacc.query.api.repository;

import com.supplieswind.bankacc.core.model.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<BankAccount, String> {
}
