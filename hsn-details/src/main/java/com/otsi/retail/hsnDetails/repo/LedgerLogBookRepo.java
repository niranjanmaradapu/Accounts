package com.otsi.retail.hsnDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;

@Repository
public interface LedgerLogBookRepo extends JpaRepository<LedgerLogBook, Long> {

	LedgerLogBook findByAccountingBookAccountingBookId(Long accountingBookId);

}
