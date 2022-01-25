package com.otsi.retail.hsnDetails.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.service.ReportService;
import com.otsi.retail.hsnDetails.vo.ReportsVo;

@RestController
@RequestMapping("/reports")
public class ReportController {
	
	private Logger log = LoggerFactory.getLogger(ReportController.class);

	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/debitNotesByStores")
	public GateWayResponse<?> debitNotesByStores() {
		log.info("Received Request to debitNotesByStores");
		List<ReportsVo> allCreditDebitNotes = reportService.debitNotesByStores();
		return new GateWayResponse<>("fetching all debitnotes sucessfully", allCreditDebitNotes);

	}

	@GetMapping("/usedAndBalancedAmountByStores")
	public GateWayResponse<?> usedAndBalancedAmountByStores() {
		log.info("Received Request to usedAndBalancedAmountByStores");
		List<ReportsVo> allCreditDebitNotes = reportService.usedAndBalancedAmountByStores();
		return new GateWayResponse<>("fetching all creditnotes sucessfully", allCreditDebitNotes);

	}

}
