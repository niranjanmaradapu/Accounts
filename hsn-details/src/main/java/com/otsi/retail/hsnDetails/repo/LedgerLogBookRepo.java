package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.enums.PaymentStatus;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;

@Repository
public interface LedgerLogBookRepo extends JpaRepository<LedgerLogBook, Long> {

	LedgerLogBook findByLedgerLogBookId(Long ledgerLogBookId);

	Page<LedgerLogBook> findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountType(Long customerId,
			LocalDateTime fromTime, LocalDateTime fromTime1, Long storeId, AccountType accountType, Pageable page);

	Page<LedgerLogBook> findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
			Long customerId, LocalDateTime fromTime, LocalDateTime toTime, Long storeId, AccountType accountType,
			Pageable page);

	Page<LedgerLogBook> findByCustomerIdAndStoreIdAndAccountTypeOrderByCreatedDateDesc(Long customerId, Long storeId,
			AccountType accountType, Pageable pageable);

	List<LedgerLogBook> findByAccountingBookId(Long accountingBookId);

	Page<LedgerLogBook> findByCustomerIdAndAccountType(Long customerId, AccountType accountType, Pageable page);

	LedgerLogBook findByPaymentId(String razorPayId);

	LedgerLogBook findByReferenceNumber(String referenceNumber);

	LedgerLogBook findTopByCustomerIdInAndStoreIdAndAccountType(List<Long> userIds, Long storeId,
			AccountType accountType);

	// LedgerLogBook findByCustomerIdAndStoreIdAndAccountType(Long userId, Long
	// storeId, AccountType accountType);

	LedgerLogBook findByCustomerIdAndStoreIdAndAccountType(Long userId, Long storeId, AccountType accountType);

	List<LedgerLogBook> findByAccountType(AccountType accountType);

	List<LedgerLogBook> findByStoreIdAndAccountType(long id, AccountType accountType);

}
