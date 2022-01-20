package com.otsi.retail.hsnDetails.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.otsi.retail.hsnDetails.exceptions.DataNotFoundException;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;

@Service
public interface CreditDebitNotesService {

	String saveCreditDebitNotes(CreditDebitNotesVo debitNotesVo) throws DataNotFoundException;

	CreditDebitNotesVo getMobileNumber(String mobileNumber);

	List<CreditDebitNotes> getAllCreditDebitNotes();

	List<CreditDebitNotesVo> saveListCreditDebitNotes(List<CreditDebitNotesVo> debitNotesVos);

	String deleteCreditDebitNotes(Long creditDebitId);

	List<CreditDebitNotesVo> getAllCreditNotes(CreditDebitNotesVo vo);

	List<CreditDebitNotesVo> getAllDebitNotes(CreditDebitNotesVo vo);

	String updateCreditDebitNotes(UpdateCreditRequest notesVo);

	String updateNotes(CreditDebitNotesVo vo);

}
