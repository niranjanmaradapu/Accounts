package com.otsi.retail.hsnDetails.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.model.CreditDebitNotes;

@Repository
public interface CreditDebitNotesRepo extends JpaRepository<CreditDebitNotes, Long> {

	Optional<CreditDebitNotes> findByCreditDebitId(Long creditDebitId);

	List<CreditDebitNotes> findByCreatedDateBetweenAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, Long storeId, String creditDebit);

	CreditDebitNotes findByMobileNumber(String mobileNumber);

	CreditDebitNotes findByMobileNumberAndCreditDebit(String mobileNumber, String creditDebit);

	List<CreditDebitNotes> findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(
			LocalDate fromDate, LocalDate toDate, String mobileNumber, Long storeId, String creditDebit);

	List<CreditDebitNotes> findAllByStoreIdAndCreditDebit(Long storeId, String creditDebit);

	CreditDebitNotes findByMobileNumberAndStoreIdAndCreditDebit(String mobileNumber, Long storeId, String creditDebit);

	List<CreditDebitNotes> findByCreditDebit(String creditDebit);

	List<CreditDebitNotes> findByStoreIdAndCreditDebit(Long x, String creditDebit);

	CreditDebitNotes findByMobileNumberAndCustomerId(String mobileNumber, Long customerId);


}
