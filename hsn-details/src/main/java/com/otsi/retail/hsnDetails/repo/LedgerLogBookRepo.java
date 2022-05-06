package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;

@Repository
public interface LedgerLogBookRepo extends JpaRepository<LedgerLogBook, Long> {

	LedgerLogBook findByLedgerLogBookId(Long ledgerLogBookId);

	List<LedgerLogBook> findByCreatedDateBetweenAndStoreIdAndAccountType(LocalDateTime fromTime,
			LocalDateTime fromTime1, Long storeId, AccountType accountType);

	List<LedgerLogBook> findByCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
			LocalDateTime fromTime, LocalDateTime toTime, Long storeId, AccountType accountType);

	List<LedgerLogBook> findByStoreIdAndAccountType(Long storeId, AccountType accountType);

	

}
