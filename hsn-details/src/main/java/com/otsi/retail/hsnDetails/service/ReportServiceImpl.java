package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.vo.ReportsVo;

@Component
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CreditDebitNotesRepo creditDebitNotesRepo;

	@Override
	public List<ReportsVo> debitNotesByStores() {
		String creditDebit = "D";
		List<ReportsVo> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);
		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		storeIds.stream().forEach(x -> {
			List<CreditDebitNotes> store = creditDebitNotesRepo.findByStoreIdAndCreditDebit(x, creditDebit);
			long amount = store.stream().mapToLong(a -> a.getActualAmount()).sum();
			ReportsVo report = new ReportsVo();
			report.setDAmount(amount);
			report.setStoreId(x);
			reports.add(report);
		});

		return reports;
	}

	@Override
	public List<ReportsVo> usedAndBalancedAmountByStores() {
		String creditDebit = "C";
		List<ReportsVo> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);

		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		storeIds.stream().forEach(x -> {
			String status = "used";
			List<CreditDebitNotes> store = creditDebitNotesRepo.findByStoreIdAndStatus(x, status);
			long transactionAmount = store.stream().mapToLong(r -> r.getTransactionAmount()).sum();

			List<CreditDebitNotes> store1 = creditDebitNotesRepo.findByStoreIdAndFlag(x, Boolean.TRUE);
			long actualAmount = store1.stream().mapToLong(r -> r.getActualAmount()).sum();
			ReportsVo report = new ReportsVo();
			report.setActualAmount(actualAmount);
			report.setTransactionAmount(transactionAmount);
			report.setStoreId(x);
			reports.add(report);
		});

		return reports;
	}

}
