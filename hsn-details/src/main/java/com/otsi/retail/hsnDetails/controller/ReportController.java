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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "ReportController", description = "REST APIs related to ReportsVo !!!!")
@RestController
@RequestMapping("/reports")
public class ReportController {
	
	private Logger log = LoggerFactory.getLogger(ReportController.class);

	
	@Autowired
	private ReportService reportService;
	
	@ApiOperation(value = "debitNotesByStores", notes = "fetching debit notes by stores", response = ReportsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = ReportsVo.class, responseContainer = "List") })
	@GetMapping("/debitNotesByStores")
	public GateWayResponse<?> debitNotesByStores() {
		log.info("Received Request to debitNotesByStores");
		List<ReportsVo> allCreditDebitNotes = reportService.debitNotesByStores();
		return new GateWayResponse<>("fetching all debitnotes sucessfully", allCreditDebitNotes);

	}

	@ApiOperation(value = "usedAndBalancedAmountByStores", notes = "fetching used amount and balanced amount by stores", response = ReportsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = ReportsVo.class, responseContainer = "List") })
	@GetMapping("/usedAndBalancedAmountByStores")
	public GateWayResponse<?> usedAndBalancedAmountByStores() {
		log.info("Received Request to usedAndBalancedAmountByStores");
		List<ReportsVo> allCreditDebitNotes = reportService.usedAndBalancedAmountByStores();
		return new GateWayResponse<>("fetching all creditnotes sucessfully", allCreditDebitNotes);

	}

}
