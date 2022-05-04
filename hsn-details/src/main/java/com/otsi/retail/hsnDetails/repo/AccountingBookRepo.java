package com.otsi.retail.hsnDetails.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.AccountingBook;

@Repository
public interface AccountingBookRepo extends JpaRepository<AccountingBook, Long> {

	Optional<AccountingBook> findByAccountingBookId(Long accountingBookId);

	List<AccountingBook> findByAccountTypeAndStoreId(AccountType accountType, Long storeId);

}
