package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;

@Repository
public interface LedgerLogBookRepo extends JpaRepository<LedgerLogBook, Long> {

	LedgerLogBook findByLedgerLogBookId(Long ledgerLogBookId);

	Page<LedgerLogBook> findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountType(Long customerId,
			LocalDateTime fromTime, LocalDateTime fromTime1, Long storeId, AccountType accountType, Pageable page);

	Page<LedgerLogBook> findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
			Long customerId, LocalDateTime fromTime, LocalDateTime toTime, Long storeId, AccountType accountType,
			Pageable page);

	Page<LedgerLogBook> findByCustomerIdAndStoreIdAndAccountType(Long customerId, Long storeId, AccountType accountType,
			Pageable page);

	List<LedgerLogBook> findByAccountingBookId(Long accountingBookId);

	Page<LedgerLogBook> findByCustomerIdAndAccountType(Long customerId, AccountType accountType, Pageable page);

}
