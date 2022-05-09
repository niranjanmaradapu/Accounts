package com.otsi.retail.hsnDetails.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.exceptions.DataNotFoundException;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.vo.AccountingBookVo;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVo;
import com.otsi.retail.hsnDetails.vo.SearchFilterVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;

@Service
public interface CreditDebitNotesService {

	String saveCreditDebitNotes(CreditDebitNotesVo debitNotesVo) throws DataNotFoundException;

	List<CreditDebitNotesVo> getCreditNotes(String mobileNumber, Long customerId);

	List<CreditDebitNotes> getAllCreditDebitNotes();

	List<CreditDebitNotesVo> saveListCreditDebitNotes(List<CreditDebitNotesVo> debitNotesVos);

	String deleteCreditDebitNotes(Long creditDebitId);

	List<CreditDebitNotesVo> getAllCreditNotes(CreditDebitNotesVo vo);

	List<CreditDebitNotesVo> getAllDebitNotes(CreditDebitNotesVo vo);

	String updateCreditDebitNotes(UpdateCreditRequest notesVo);

	String updateNotes(CreditDebitNotesVo vo);

	LedgerLogBookVo saveNotes(LedgerLogBookVo ledgerLogBookVo);

	List<AccountingBookVo> getNotes(AccountType accountType, Long storeId);

	LedgerLogBookVo update(LedgerLogBookVo ledgerLogBookVo);

	Page<AccountingBookVo> getAllNotes(SearchFilterVo searchFilterVo, Pageable pageable);

	Page<LedgerLogBookVo> getAllLedgerLogs(SearchFilterVo searchFilterVo , Pageable page);

	//List<AccountingBookVo> getAllNotes(SearchFilterVo searchFilterVo, AccountType accountType);

	

}
