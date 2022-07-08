package com.otsi.retail.hsnDetails.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.exceptions.DataNotFoundException;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.vo.AccountingBookVO;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVO;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVO;
import com.otsi.retail.hsnDetails.vo.SearchFilterVO;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;

@Service
public interface CreditDebitNotesService {

	String saveCreditDebitNotes(CreditDebitNotesVO debitNotesVo) throws DataNotFoundException;

	List<AccountingBookVO> getCreditNotes(String mobileNumber, Long customerId);

	List<CreditDebitNotes> getAllCreditDebitNotes();

	List<CreditDebitNotesVO> saveListCreditDebitNotes(List<CreditDebitNotesVO> debitNotesVos);

	String deleteCreditDebitNotes(Long creditDebitId);

	List<CreditDebitNotesVO> getAllCreditNotes(CreditDebitNotesVO vo);

	List<CreditDebitNotesVO> getAllDebitNotes(CreditDebitNotesVO vo);

	String updateCreditDebitNotes(UpdateCreditRequest notesVo);

	String updateNotes(CreditDebitNotesVO vo);

	LedgerLogBookVO saveNotes(LedgerLogBookVO ledgerLogBookVo);

	List<AccountingBookVO> getNotes(AccountType accountType, Long storeId);

	LedgerLogBookVO update(LedgerLogBookVO ledgerLogBookVo);

	Page<AccountingBookVO> getAllNotes(SearchFilterVO searchFilterVo, Pageable pageable);

	Page<LedgerLogBookVO> getAllLedgerLogs(SearchFilterVO searchFilterVo , Pageable page);

	Boolean paymentConfirmationFromRazorpay(String razorPayId, boolean payStatus);

}
