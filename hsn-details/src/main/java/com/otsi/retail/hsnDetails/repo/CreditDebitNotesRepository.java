package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.AccountingBook;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;

@Repository
public interface CreditDebitNotesRepository extends JpaRepository<CreditDebitNotes, Long> {

	Optional<CreditDebitNotes> findByCreditDebitId(Long creditDebitId);

	List<CreditDebitNotes> findByCreditDebit(String creditDebit);

	List<CreditDebitNotes> findByStoreIdAndCreditDebit(Long x, String creditDebit);

	List<CreditDebitNotes> findAllByMobileNumberAndCustomerIdAndFlag(String mobileNumber, Long customerId,
			boolean flag);

	List<CreditDebitNotes> findByStoreIdAndCreditDebitAndActualAmountAndTransactionAmount(Long x, String creditDebit,
			Long actualAmount, Long transactionAmount);

	CreditDebitNotes findByMobileNumberAndCustomerIdAndFlag(String mobileNumber, Long customerId, boolean flag);

	CreditDebitNotes findByMobileNumberAndStoreIdAndCreditDebitAndFlag(String mobileNumber, Long storeId,
			String creditDebit, boolean flag);

	List<CreditDebitNotes> findByStoreIdAndStatus(Long x, String status);

	List<CreditDebitNotes> findByStoreIdAndFlag(Long x, Boolean true1);

	List<CreditDebitNotes> findAllByStoreIdAndCreditDebitAndFlag(Long storeId, String creditDebit, boolean flag);

	List<CreditDebitNotes> findByCreatedDateBetweenAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, Long storeId, String creditDebit, boolean flag);

	List<CreditDebitNotes> findAllByMobileNumber(String mobileNumber);

	List<CreditDebitNotes> findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, String mobileNumber, Long storeId, String creditDebit, boolean flag);

	CreditDebitNotes findByMobileNumberAndFlag(String mobileNumber, boolean flag);

	List<CreditDebitNotes> findByCustomerIdAndFlag(Long customerId, boolean flag);

}
