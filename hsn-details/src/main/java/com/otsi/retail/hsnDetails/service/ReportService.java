package com.otsi.retail.hsnDetails.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.vo.ReportsVO;

@Service
public interface ReportService {

	List<ReportsVO> debitNotesByStores();

	List<ReportsVO> usedAndBalancedAmountByStores();

}
