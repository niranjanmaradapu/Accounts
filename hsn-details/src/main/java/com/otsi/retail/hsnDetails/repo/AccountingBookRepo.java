package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountStatus;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.AccountingBook;

@Repository
public interface AccountingBookRepo extends JpaRepository<AccountingBook, Long> {

	Optional<AccountingBook> findByAccountingBookId(Long accountingBookId);

	List<AccountingBook> findByAccountTypeAndStoreId(AccountType accountType, Long storeId);

	List<AccountingBook> findAllByCustomerId(Long customerId);

	List<AccountingBook> findByStoreIdAndAccountTypeAndLedgerLogBooksStatus(Long storeId, AccountType accountType,
			AccountStatus accountStatus);

	List<AccountingBook> findByCreatedDateBetweenAndStoreIdAndAccountTypeAndLedgerLogBooksStatusOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, Long storeId, AccountType accountType, AccountStatus accountStatus);

	List<AccountingBook> findByCreatedDateBetweenAndCustomerIdInAndStoreIdAndAccountTypeAndLedgerLogBooksStatusOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, List<Long> userIds, Long storeId, AccountType accountType,
			AccountStatus accountStatus);

	List<AccountingBook> findByCustomerIdInAndStoreIdAndAccountTypeAndLedgerLogBooksStatus(List<Long> userIds,
			Long storeId, AccountType accountType, AccountStatus accountStatus);

}
