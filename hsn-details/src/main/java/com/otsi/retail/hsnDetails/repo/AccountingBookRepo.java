package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.AccountingBook;

@Repository
public interface AccountingBookRepo extends JpaRepository<AccountingBook, Long> {

	Optional<AccountingBook> findByAccountingBookId(Long accountingBookId);

	//AccountingBook findByCustomerIdAndStoreIdAndAccountType(Long customerId, Long storeId, AccountType accountType);

	Page<AccountingBook> findByCreatedDateBetweenAndStoreIdAndAccountType(LocalDateTime fromTime,
			LocalDateTime fromTime1, Long storeId, AccountType accountType, Pageable pageable);

	Page<AccountingBook> findByCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
			LocalDateTime fromTime, LocalDateTime toTime, Long storeId, AccountType accountType, Pageable pageable);

	Page<AccountingBook> findByCreatedDateBetweenAndCustomerIdInAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
			LocalDateTime fromTime, LocalDateTime toTime, List<Long> userIds, Long storeId, AccountType accountType,
			Pageable pageable);

	Page<AccountingBook> findByStoreIdAndAccountType(Long storeId, AccountType accountType, Pageable pageable);

	Page<AccountingBook> findByCustomerIdInAndStoreIdAndAccountType(List<Long> userIds, Long storeId,
			AccountType accountType, Pageable pageable);

	AccountingBook findByCustomerIdAndAccountType(Long customerId, AccountType accountType);

	List<AccountingBook> findByAccountTypeAndStoreId(AccountType accountType, Long storeId);

	
}
