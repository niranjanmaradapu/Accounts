package com.otsi.retail.hsnDetails.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.otsi.retail.hsnDetails.config.Config;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.service.CreditDebitNotesService;
import com.otsi.retail.hsnDetails.vo.AccountingBookVo;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVo;
import com.otsi.retail.hsnDetails.vo.SearchFilterVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author vasavi
 *
 */
@Api(value = "CreditDebitNotesController", description = "REST APIs related to CreditDebitNotes !!!!")
@RestController
@RequestMapping("/accounting")
public class CreditDebitNotesController {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesController.class);

	@Autowired
	private CreditDebitNotesService creditDebitNotesService;

	@ApiOperation(value = "saveCreditDebitNotes", notes = "saving credit/debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PostMapping("/saveCreditDebitNotes")
	public GateWayResponse<?> saveCreditDebitNotes(@RequestBody CreditDebitNotesVo debitNotesVo) {
		log.info("Received Request to saveDebitNotes : " + debitNotesVo);
		String debitNotesSave = creditDebitNotesService.saveCreditDebitNotes(debitNotesVo);
		return new GateWayResponse<>("saved notes successfully", debitNotesSave);

	}

	@ApiOperation(value = "getCreditNotes", notes = "fetching credit notes using customerId", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "List") })
	@GetMapping("/getCreditNotes")
	public GateWayResponse<?> getMobileNumber(@RequestParam("mobileNumber") String mobileNumber,
			@RequestParam("customerId") Long customerId) {
		log.info("Received Request to getCreditNotes : " + mobileNumber + "and" + customerId);
		List<CreditDebitNotesVo> mobNo = creditDebitNotesService.getCreditNotes(mobileNumber, customerId);
		return new GateWayResponse<>("fetching  notes successfully with id", mobNo);
	}

	@ApiOperation(value = "getAllCreditDebitNotes", notes = "fetching all credit and debit notes", response = CreditDebitNotes.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotes.class, responseContainer = "List") })
	@GetMapping("/getAllCreditDebitNotes")
	public GateWayResponse<?> getAllCreditDebitNotes() {
		log.info("Received Request to getAllCreditDebitNotes");
		List<CreditDebitNotes> allCreditDebitNotes = creditDebitNotesService.getAllCreditDebitNotes();
		return new GateWayResponse<>("fetching all  notes sucessfully", allCreditDebitNotes);

	}

	@ApiOperation(value = "saveListCreditDebitNotes", notes = "adding bulk of credit and debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/saveListCreditDebitNotes")
	public GateWayResponse<?> saveListCreditDebitNotes(@RequestBody List<CreditDebitNotesVo> creditDebitNotesVo) {
		log.info("Received Request to saveListCreditDebitNotes:" + creditDebitNotesVo);
		List<CreditDebitNotesVo> saveVoList = creditDebitNotesService.saveListCreditDebitNotes(creditDebitNotesVo);
		return new GateWayResponse<>("saving list of notes", saveVoList);

	}

	@ApiOperation(value = "updateCreditDebitNotes", notes = "updating credit/debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PostMapping(value = "/updateCreditDebitNotes")
	public GateWayResponse<?> updateCreditDebitNotes(@RequestBody UpdateCreditRequest vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateCreditDebitNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}

	@ApiOperation(value = "updateNotes", notes = "updating credit/debit notes from newsale", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PutMapping(value = "/updateNotes")
	public GateWayResponse<?> updateNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}

	@ApiOperation(value = "deleteCreditDebitNotes", notes = "deleting credit/debit notes using id", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "String") })
	@DeleteMapping("/deleteCreditDebitNotes")
	public GateWayResponse<?> deleteCreditDebitNotes(@RequestParam("creditDebitId") Long creditDebitId) {
		log.info("Recieved request to deleteCreditDebitNotes:" + creditDebitId);
		String deleteNotes = creditDebitNotesService.deleteCreditDebitNotes(creditDebitId);
		return new GateWayResponse<>("notes deleted successfully", deleteNotes);

	}

	@ApiOperation(value = "getAllCreditNotes", notes = "fetching all credit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/getAllCreditNotes")
	public GateWayResponse<?> getAllCreditNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllCreditNotes:" + vo);
		List<CreditDebitNotesVo> allCreditNotes = creditDebitNotesService.getAllCreditNotes(vo);
		return new GateWayResponse<>("fetching all credit notes details sucessfully", allCreditNotes);
	}

	@ApiOperation(value = "getAllDebitNotes", notes = "fetching all debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/getAllDebitNotes")
	public GateWayResponse<?> getAllDebitNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllDebitNotes:" + vo);
		List<CreditDebitNotesVo> allDebitNotes = creditDebitNotesService.getAllDebitNotes(vo);
		return new GateWayResponse<>("fetching all debit notes details sucessfully", allDebitNotes);
	}

	@ApiOperation(value = "save", notes = "saving credit/debit notes", response = AccountingBookVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = AccountingBookVo.class, responseContainer = "String") })
	@PostMapping("/save")
	public ResponseEntity<?> saveNotes(@RequestBody LedgerLogBookVo ledgerLogBookVo) {
		log.info("Received Request to saveNotes : " + ledgerLogBookVo);
		if (StringUtils.isEmpty(ledgerLogBookVo.getAccountType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "acounting type is required");
		}
		if (ledgerLogBookVo.getAccountType().equals(AccountType.CREDIT)) {
			ledgerLogBookVo.setTransactionType(AccountType.CREDIT);
		
		} else {
			if (ledgerLogBookVo.getAccountType().equals(AccountType.DEBIT)) {
				ledgerLogBookVo.setTransactionType(AccountType.DEBIT);
				
			}
		}
		LedgerLogBookVo notesSave = creditDebitNotesService.saveNotes(ledgerLogBookVo);
		return ResponseEntity.ok(notesSave);

	}

	@RabbitListener(queues = "return_credit_queue")
	public void returnCreditNotes(@Payload LedgerLogBookVo ledgerLogBookVo) {
		saveNotes(ledgerLogBookVo);
	}

	@ApiOperation(value = "sale", notes = "saving credit/debit notes", response = AccountingBookVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = AccountingBookVo.class, responseContainer = "String") })
	@PostMapping("/sale")
	public ResponseEntity<?> sale(@RequestBody LedgerLogBookVo ledgerLogBookVo) {
		log.info("Received Request to sale : " + ledgerLogBookVo);

		if (StringUtils.isEmpty(ledgerLogBookVo.getAccountType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "acounting type is required");
		}
		if (ledgerLogBookVo.getAccountType().equals(AccountType.CREDIT)) {
			ledgerLogBookVo.setTransactionType(AccountType.DEBIT);
		} else {
			if (ledgerLogBookVo.getAccountType().equals(AccountType.DEBIT)) {
				ledgerLogBookVo.setTransactionType(AccountType.CREDIT);
			}
		}
		LedgerLogBookVo ledgerLogBookSave=creditDebitNotesService.saveNotes(ledgerLogBookVo);
		return ResponseEntity.ok(ledgerLogBookSave);
	}
	
	@RabbitListener(queues = "accounting_queue")
	private void creditUsedFromNewsale(LedgerLogBookVo ledgerLogBookVo) {
		sale(ledgerLogBookVo);
	}

	@ApiOperation(value = "getNotes", notes = "fetching notes using account type and storeId", response = AccountingBookVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = AccountingBookVo.class, responseContainer = "List") })
	@GetMapping("/")
	public ResponseEntity<?> getNotes(@RequestParam("storeId") Long storeId,
			@RequestParam("accountType") AccountType accountType) {
		log.info("Received Request to getNotes : " + accountType + "" + storeId);
		List<AccountingBookVo> accountingBookVo = creditDebitNotesService.getNotes(accountType, storeId);
		return ResponseEntity.ok(accountingBookVo);
	}

	@ApiOperation(value = "update", notes = "updating credit/debit notes from newsale", response = AccountingBookVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = AccountingBookVo.class, responseContainer = "String") })
	@PutMapping(value = "/update")
	public ResponseEntity<?> update(@RequestBody LedgerLogBookVo ledgerLogBookVo) {
		log.info("Recieved request to updateCreditDebitNotes:" + ledgerLogBookVo);
		LedgerLogBookVo updateNotes = creditDebitNotesService.update(ledgerLogBookVo);
		return ResponseEntity.ok(updateNotes);

	}

	/*
	 * @ApiOperation(value = "delete", notes =
	 * "deleting credit/debit notes using id", response = AccountingBookVo.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
	 * 
	 * @ApiResponse(code = 200, message = "Successful retrieval", response =
	 * AccountingBookVo.class, responseContainer = "String") })
	 * 
	 * @DeleteMapping("/delete") public GateWayResponse<?>
	 * delete(@RequestParam("accountBookingId") Long accountBookingId) {
	 * log.info("Recieved request to deleteCreditDebitNotes:" + accountBookingId);
	 * String deleteNotes = creditDebitNotesService.delete(accountBookingId); return
	 * new GateWayResponse<>("notes deleted successfully", deleteNotes);
	 * 
	 * }
	 */

	@ApiOperation(value = "", notes = "fetching all notes", response = AccountingBookVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = AccountingBookVo.class, responseContainer = "List") })

	@PostMapping
	public ResponseEntity<?> getAllNotes(@RequestBody SearchFilterVo searchFilterVo, Pageable pageable) {
		log.info("Recieved request to getAllNotes:" + searchFilterVo);
		Page<AccountingBookVo> allNotes = creditDebitNotesService.getAllNotes(searchFilterVo, pageable);
		return ResponseEntity.ok(allNotes);
	}

	/**
	 * 
	 * @param searchFilterVo
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "ledger-logs", notes = "fetching all ledger logs", response = LedgerLogBookVo.class)

	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),

			@ApiResponse(code = 200, message = "Successful retrieval", response = LedgerLogBookVo.class, responseContainer = "List") })
	@PostMapping("/ledger-logs")
	public ResponseEntity<?> getAllLedgerLogs(@RequestBody SearchFilterVo searchFilterVo, Pageable page) {
		log.info("Recieved request to getAllNotes:" + searchFilterVo);
		Page<LedgerLogBookVo> ledgerLogs = creditDebitNotesService.getAllLedgerLogs(searchFilterVo, page);
		return ResponseEntity.ok(ledgerLogs);
	}

	@PostMapping("payconfirmation")
	public ResponseEntity<?> paymentConfirmationFromRazorpay(@RequestParam String razorPayId,
			@RequestParam boolean payStatus) {
		log.info("Received payment confirmation for razorpayId :" + razorPayId);

		Boolean response = creditDebitNotesService.paymentConfirmationFromRazorpay(razorPayId, payStatus);
		return ResponseEntity.ok(response);

	}

}
